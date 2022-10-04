package pl.edu.pg.eti.presentation.fragment

import android.graphics.*
import android.os.Bundle
import android.view.*
import android.view.ViewTreeObserver.OnGlobalLayoutListener
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.navGraphViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.rabbitmq.client.DeliverCallback
import com.rabbitmq.client.Delivery
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import pl.edu.pg.eti.R
import pl.edu.pg.eti.databinding.FragmentDrawingBinding
import pl.edu.pg.eti.domain.model.ScoreboardItemModel
import pl.edu.pg.eti.domain.model.events.*
import pl.edu.pg.eti.presentation.adapter.MessageRecyclerViewAdapter
import pl.edu.pg.eti.presentation.viewmodel.GameViewModel
import timber.log.Timber
import java.nio.charset.StandardCharsets

@AndroidEntryPoint
class DrawingFragment : Fragment() {
    private lateinit var binding: FragmentDrawingBinding
    private var floatStartX = -1f
    private var floatStartY = -1f
    private var floatEndX = -1f
    private var floatEndY = -1f
    private lateinit var bitmap: Bitmap
    private lateinit var canvas: Canvas
    private var paint = Paint()
    private var adapter = MessageRecyclerViewAdapter(arrayListOf())
    private val viewModel: GameViewModel by navGraphViewModels(R.id.game_nav_graph) { defaultViewModelProviderFactory }
    private var timeLeft = 60_000L
    private var timeSyncJob: Job? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_drawing,
            container,
            false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //viewModel.sessionManager.declareNewQueue("queue123")
        setupDrawingListener()
        setupAdapter()
        waitForImageView()

        consumeMessages()
        binding.tvRound.text =
            "${viewModel.roundsPassed}/${viewModel.roundsLeft + viewModel.roundsPassed}"
        binding.tvKeyword.text = viewModel.keyword

    }

    fun consumeMessages() {
        viewModel.callback = {
            when (it.subSequence(0, 3)) {
                "CME" -> {
                    val message = ChatMessageEvent(it)
                    requireActivity().runOnUiThread {
                        adapter.addMessage(message)
                        binding.chatRecyclerView.smoothScrollToPosition(0)
                    }
                }
                "TSE" -> {
                    val message = TimeSyncEvent(it)
                    timeSyncJob?.cancel()
                    timeSyncJob = lifecycleScope.launch(Dispatchers.Main) {
                        timeLeft = message.timeLeft
                        while (timeLeft > 0) {
                            binding.tvTimeLeft.text = timeLeft.toString()
                            delay(250)
                            timeLeft -= 250
                        }
                        binding.tvTimeLeft.text = "0"
                    }
                }
                "PHE" -> {
                    val message = PlayerHitEvent(it)
                    requireActivity().runOnUiThread {
                        adapter.addMessage(message)
                        binding.chatRecyclerView.smoothScrollToPosition(0)
                    }
                }
                "SBE" -> {
                    val message = ScoreboardEvent(it)
                    val newList = message.scoreboard.mapIndexed { index, scoreboardRow ->
                        ScoreboardItemModel(
                            index.toString(),
                            scoreboardRow.nickname,
                            scoreboardRow.roundScore.toString()
                        )
                    }
                    viewModel.scoreboardList = newList
                    requireActivity().runOnUiThread {
                        findNavController().navigate(R.id.action_drawingFragment_to_scoreboardFragment)
                    }
                }
                "FSE" -> {
                    val message = ScoreboardEvent(it)
                    val newList = message.scoreboard.mapIndexed { index, scoreboardRow ->
                        ScoreboardItemModel(
                            index.toString(),
                            scoreboardRow.nickname,
                            scoreboardRow.totalScore.toString()
                        )
                    }
                    viewModel.scoreboardList = newList
                    requireActivity().runOnUiThread {
                        findNavController().navigate(R.id.action_drawingFragment_to_scoreboardFragment)
                    }
                }
            }
            Timber.d(it)
        }
    }


    private fun waitForImageView() {
        val viewTreeObserver = binding.imageView.viewTreeObserver
        if (viewTreeObserver.isAlive) {
            viewTreeObserver.addOnGlobalLayoutListener(object : OnGlobalLayoutListener {
                override fun onGlobalLayout() {
                    view!!.viewTreeObserver.removeOnGlobalLayoutListener(this)
                    bitmap = Bitmap.createBitmap(
                        binding.imageView.measuredWidth,
                        binding.imageView.measuredWidth,
                        Bitmap.Config.RGB_565
                    )
                    setStartingValues()
                }
            })
        }
    }

    private fun setupAdapter() {
        binding.chatRecyclerView.layoutManager = LinearLayoutManager(context)
        binding.chatRecyclerView.adapter = adapter
    }

    private fun setStartingValues() {
        canvas = Canvas(bitmap)
        canvas.drawColor(Color.parseColor("#FFFCE8"))
        paint.color = Color.BLACK
        paint.isAntiAlias = true
        paint.style = Paint.Style.STROKE
        paint.strokeCap = Paint.Cap.ROUND
        paint.setStrokeWidth(10f)
    }

    fun clearCanvas() {
        canvas.drawColor(Color.parseColor("#FFFCE8"))
    }

    private fun setupDrawingListener() {
        binding.imageView.setOnTouchListener(object : View.OnTouchListener {
            override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                when (event?.action) {
                    MotionEvent.ACTION_DOWN -> {
                        floatStartX = event.x
                        floatStartY = event.y
                    }
                    MotionEvent.ACTION_MOVE -> {
                        floatEndX = event.x
                        floatEndY = event.y
                        drawPainting()
                        floatStartX = event.x
                        floatStartY = event.y
                    }
                    MotionEvent.ACTION_UP -> {
                        floatEndX = event.x
                        floatEndY = event.y
                        drawPainting()
                    }
                }
                return true
            }
        }
        )


        fun onPaintButtonClick(view: View) {
            paint.color = view.backgroundTintList!!.defaultColor
        }

        binding.btnBlack.setOnClickListener(::onPaintButtonClick)
        binding.btnBlue.setOnClickListener(::onPaintButtonClick)
        binding.btnGreen.setOnClickListener(::onPaintButtonClick)
        binding.btnRed.setOnClickListener(::onPaintButtonClick)

        binding.btnClear.setOnClickListener {
            clearCanvas()
            viewModel.sendClearCanvas()
        }

        binding.btnPlus.setOnClickListener {
            paint.strokeWidth =
                if (paint.strokeWidth + 2f > 20f) 20f else paint.strokeWidth + 2f

        }
        binding.btnMinus.setOnClickListener {
            paint.strokeWidth = if (paint.strokeWidth - 2f < 6) 6f else paint.strokeWidth - 2f
            //adapter.addMessage(MessageModel("elo", "content"))
        }
    }

    private fun drawPainting() {
        canvas.drawLine(floatStartX, floatStartY, floatEndX, floatEndY, paint)
        binding.imageView.setImageBitmap(bitmap)
        viewModel.sendDrawLineEvent(
            floatStartX,
            floatStartY,
            floatEndX,
            floatEndY,
            paint.color,
            paint.strokeWidth
        )
    }
}