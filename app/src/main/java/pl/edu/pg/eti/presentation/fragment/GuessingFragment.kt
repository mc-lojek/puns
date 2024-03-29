package pl.edu.pg.eti.presentation.fragment

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver.OnGlobalLayoutListener
import android.view.inputmethod.EditorInfo
import android.widget.EditText
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
import pl.edu.pg.eti.databinding.FragmentGuessingBinding
import pl.edu.pg.eti.domain.model.ScoreboardItemModel
import pl.edu.pg.eti.domain.model.events.*
import pl.edu.pg.eti.domain.util.BASIC_CANVAS_SIZE
import pl.edu.pg.eti.presentation.adapter.MessageRecyclerViewAdapter
import pl.edu.pg.eti.presentation.viewmodel.GameViewModel
import timber.log.Timber
import java.nio.charset.StandardCharsets

@AndroidEntryPoint
class GuessingFragment : Fragment() {

    private lateinit var binding: FragmentGuessingBinding
    private lateinit var bitmap: Bitmap
    private lateinit var canvas: Canvas
    private var paint = Paint()
    private val viewModel: GameViewModel by navGraphViewModels(R.id.game_nav_graph) { defaultViewModelProviderFactory }
    private var adapter = MessageRecyclerViewAdapter(arrayListOf())
    private var timeLeft = 60_000L
    private var timeSyncJob: Job? = null
    private var screenRatio = -1f

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_guessing,
            container,
            false
        )
        return binding.root
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        waitForImageView()
        binding.btnSend.setOnClickListener {
            if(!binding.textGuess.text.trim().isEmpty()){
                viewModel.sendGuess(binding.textGuess.text.toString())
                binding.textGuess.text.clear()
            }
        }
        binding.textGuess.onSubmit {
            binding.btnSend.performClick()
        }

        setupAdapter()
        consumeMessages()
        timeLeft=viewModel.basicRoundTime
        binding.tvRound.text =
            "${viewModel.roundsPassed+1}/${viewModel.roundsLeft + viewModel.roundsPassed+1}"
        timeSyncJob = lifecycleScope.launch(Dispatchers.Main) {
            while (timeLeft > 0) {
                binding.tvTimeLeft.text = ((timeLeft+10) / 1000).toString()
                delay(250)
                timeLeft -= 250
            }
            binding.tvTimeLeft.text = "0"
        }
    }

    fun consumeMessages() {
        viewModel.callback = {
            when (it.subSequence(0, 3)) {
                "DLE" -> {
                    val singleLineMessageModel = menageCanvaMessage(it)
                    paint = Paint()
                    drawPainting(
                        singleLineMessageModel.startX * screenRatio,
                        singleLineMessageModel.startY * screenRatio,
                        singleLineMessageModel.endX * screenRatio,
                        singleLineMessageModel.endY * screenRatio,
                        singleLineMessageModel.paintColor,
                        singleLineMessageModel.paintSize
                    )
                }
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
                            binding.tvTimeLeft.text = (timeLeft / 1000).toString()
                            delay(250)
                            timeLeft -= 250
                        }
                        binding.tvTimeLeft.text = "0"
                    }
                }
                "CCE" -> {
                    clearCanvas()
                }
                "PHE" -> {
                    val message = PlayerHitEvent(it)
                    requireActivity().runOnUiThread {
                        if(message.nickname.equals(viewModel.sessionManager.playerNickname)) {
                            binding.btnSend.isEnabled=false
                        }
                        adapter.addMessage(message)
                        binding.chatRecyclerView.smoothScrollToPosition(0)
                    }
                }
                "SBE" -> {
                    val message = ScoreboardEvent(it)
                    val newList = message.scoreboard.mapIndexed { index, scoreboardRow ->
                        ScoreboardItemModel(
                            (index+1).toString(),
                            scoreboardRow.nickname,
                            scoreboardRow.roundScore.toString()
                        )
                    }
                    viewModel.scoreboardList = newList
                    requireActivity().runOnUiThread {
                        val bundle = Bundle()
                        bundle.putBoolean("isFinal", false)
                        findNavController().navigate(R.id.action_guessingFragment_to_scoreboardFragment,bundle)
                    }
                }
                "FSE" -> {
                    val message = ScoreboardEvent(it)
                    val newList = message.scoreboard.mapIndexed { index, scoreboardRow ->
                        ScoreboardItemModel(
                            (index+1).toString(),
                            scoreboardRow.nickname,
                            scoreboardRow.totalScore.toString()
                        )
                    }
                    viewModel.scoreboardList = newList
                    requireActivity().runOnUiThread {
                        val bundle = Bundle()
                        bundle.putBoolean("isFinal", true)
                        findNavController().navigate(R.id.action_guessingFragment_to_scoreboardFragment,bundle)
                    }
                }
                "BIE" -> {
                    val message = BlockInteractionEvent(it)
                    requireActivity().runOnUiThread {
                        adapter.addMessage(ChatMessageEvent("",message.keyword,5))
                        binding.chatRecyclerView.smoothScrollToPosition(0)
                        binding.btnSend.isEnabled=false
                    }
                }
            }

            Timber.d(it)
        }
    }

    fun clearCanvas() {
        canvas.drawColor(Color.parseColor("#FFFCE8"))
    }

    private fun waitForImageView() {
        val viewTreeObserver = binding.canvasIv.viewTreeObserver
        if (viewTreeObserver.isAlive) {
            viewTreeObserver.addOnGlobalLayoutListener(object : OnGlobalLayoutListener {
                override fun onGlobalLayout() {
                    view!!.viewTreeObserver.removeOnGlobalLayoutListener(this)
                    bitmap = Bitmap.createBitmap(
                        binding.canvasIv.measuredWidth,
                        binding.canvasIv.measuredWidth,
                        Bitmap.Config.RGB_565
                    )
                    setStartingValues()
                }
            })
        }
    }

    private fun setStartingValues() {
        screenRatio = binding.canvasIv.width / BASIC_CANVAS_SIZE
        canvas = Canvas(bitmap)
        canvas.drawColor(Color.parseColor("#FFFCE8"))
        paint.color = Color.BLACK
        paint.isAntiAlias = true
        paint.style = Paint.Style.STROKE
        paint.strokeCap = Paint.Cap.ROUND
        paint.setStrokeWidth(10f)

    }

    private fun setupAdapter() {
        binding.chatRecyclerView.layoutManager = LinearLayoutManager(context)
        binding.chatRecyclerView.adapter = adapter
    }

    private fun drawPainting(
        floatStartX: Float,
        floatStartY: Float,
        floatEndX: Float,
        floatEndY: Float,
        paintColor: Int,
        paintSize: Float
    ) {
        val paint1 = Paint()
        paint1.apply {
            color = paintColor
            isAntiAlias = true
            style = Paint.Style.STROKE
            strokeCap = Paint.Cap.ROUND
            setStrokeWidth(paintSize)
        }
        requireActivity().runOnUiThread {
            canvas.drawLine(floatStartX, floatStartY, floatEndX, floatEndY, paint1)
            binding.canvasIv.setImageBitmap(bitmap)
        }
    }

    fun menageCanvaMessage(message: String): DrawLineEvent {
        val array = message.split(",")

        return DrawLineEvent(
            array[1].toFloat(),
            array[2].toFloat(),
            array[3].toFloat(),
            array[4].toFloat(),
            array[5].toInt(),
            array[6].toFloat()
        )
    }

    val deliverCallback = DeliverCallback { consumerTag: String?, delivery: Delivery ->
        requireActivity().runOnUiThread {
            val message = String(delivery.body, StandardCharsets.UTF_8)
            val array = message.split(",")
            if (array[0] == "draw") {
                val singleLineMessageModel = menageCanvaMessage(message)
                paint = Paint()
                drawPainting(
                    singleLineMessageModel.startX,
                    singleLineMessageModel.startY,
                    singleLineMessageModel.endX,
                    singleLineMessageModel.endY,
                    singleLineMessageModel.paintColor,
                    singleLineMessageModel.paintSize
                )
            }
            if (array[0] == "mess") {

            }
            Timber.d("[$consumerTag] Received: '$message'")
        }


    }



    fun EditText.onSubmit(func: () -> Unit) {
        setOnEditorActionListener { _, actionId, _ ->

            if (actionId == EditorInfo.IME_ACTION_DONE) {
                func()
            }

            true

        }
    }
}