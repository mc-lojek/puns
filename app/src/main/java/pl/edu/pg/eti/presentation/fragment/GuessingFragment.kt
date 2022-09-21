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
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.navGraphViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.rabbitmq.client.DeliverCallback
import com.rabbitmq.client.Delivery
import dagger.hilt.android.AndroidEntryPoint
import pl.edu.pg.eti.R
import pl.edu.pg.eti.databinding.FragmentGuessingBinding
import pl.edu.pg.eti.domain.model.DrawLineEvent
import pl.edu.pg.eti.domain.model.PlayerGuessEvent
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
            viewModel.sendGuess(binding.textGuess.text.toString())
            adapter.addMessage(PlayerGuessEvent("me", binding.textGuess.text.toString()))
            binding.textGuess.text.clear()
            binding.chatRecyclerView.smoothScrollToPosition(0)
        }
        setupAdapter()
        consumeMessages()
    }

    fun consumeMessages() {
        viewModel.callback = {
            val array = it.split(",")
            if (array[0] == "DRW") {
                val singleLineMessageModel = menageCanvaMessage(it)
                paint = Paint()
                drawPainting(
                    singleLineMessageModel.startX,
                    singleLineMessageModel.startY,
                    singleLineMessageModel.endX,
                    singleLineMessageModel.endY,
                    singleLineMessageModel.paintColor,
                    singleLineMessageModel.paintSize
                )
            } else if (array[0] == "CMS") {

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

    private fun setStartingValues() {
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
            binding.imageView.setImageBitmap(bitmap)
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
            println("[$consumerTag] Received: '$message'")
        }


    }


}