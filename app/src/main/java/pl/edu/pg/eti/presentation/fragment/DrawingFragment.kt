package pl.edu.pg.eti.presentation.fragment

import android.graphics.*
import android.os.Bundle
import android.view.*
import android.view.ViewTreeObserver.OnGlobalLayoutListener
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.navGraphViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.rabbitmq.client.DeliverCallback
import com.rabbitmq.client.Delivery
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import pl.edu.pg.eti.R
import pl.edu.pg.eti.databinding.FragmentDrawingBinding
import pl.edu.pg.eti.domain.manager.SessionManager
import pl.edu.pg.eti.domain.model.MessageModel
import pl.edu.pg.eti.presentation.adapter.MessageRecyclerViewAdapter
import pl.edu.pg.eti.presentation.viewmodel.DrawingViewModel
import pl.edu.pg.eti.presentation.viewmodel.GameViewModel
import timber.log.Timber
import java.nio.charset.StandardCharsets
import javax.inject.Inject

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
        //viewModel.initializeAndConsume("room-11-12",deliverCallback)//todo queue name
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //viewModel.sessionManager.declareNewQueue("queue123")
        setupDrawingListener()
        setupAdapter()
        waitForImageView()

        consumeMessages()

    }

    fun consumeMessages() {
        viewModel.callback = {
            val array = it.split(",")
            if (array[0] == "draw") {

            } else if (array[0] == "mess") {
                requireActivity().runOnUiThread {
                    adapter.addMessage(MessageModel(array[1], array[2]))
                    binding.chatRecyclerView.smoothScrollToPosition(0)
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


        binding.btnPlus.setOnClickListener {
            paint.strokeWidth =
                if (paint.strokeWidth + 2f > 20f) 20f else paint.strokeWidth + 2f

        }
        binding.btnMinus.setOnClickListener {
            paint.strokeWidth = if (paint.strokeWidth - 2f < 6) 6f else paint.strokeWidth - 2f
            //adapter.addMessage(MessageModel("elo", "content"))
        }
    }

    private fun setKeyWord(keyword: String) {
        binding.tvKeyword.text = "${resources.getString(R.string.keyword_prefix)}: ${keyword}"
    }

    private fun setTimeLeft(timeInSeconds: Int) {
        binding.tvTimeLeft.text = timeInSeconds.toString() + "s"
    }

    private fun setRoundTV(now: Int, total: Int) {
        binding.tvRound.text = now.toString() + "/" + total.toString()
    }

    private fun drawPainting() {
        canvas.drawLine(floatStartX, floatStartY, floatEndX, floatEndY, paint)
        binding.imageView.setImageBitmap(bitmap)
        viewModel.sendSingleLineModel(
            floatStartX,
            floatStartY,
            floatEndX,
            floatEndY,
            paint.color,
            paint.strokeWidth
        )
    }

    val deliverCallback = DeliverCallback { consumerTag: String?, delivery: Delivery ->
        requireActivity().runOnUiThread {
            val message = String(delivery.body, StandardCharsets.UTF_8)
            val array = message.split(",")
            if (array[0] == "draw") {

            }
            if (array[0] == "mess") {
                adapter.addMessage(MessageModel(array[1], array[2]))
                binding.chatRecyclerView.smoothScrollToPosition(0)
            }
            println("[$consumerTag] Received: '$message'")
        }


    }

}