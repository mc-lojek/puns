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
import androidx.fragment.app.viewModels
import com.rabbitmq.client.DeliverCallback
import com.rabbitmq.client.Delivery
import dagger.hilt.android.AndroidEntryPoint
import pl.edu.pg.eti.R
import pl.edu.pg.eti.databinding.FragmentGuessingBinding
import pl.edu.pg.eti.domain.model.CanvaSingleLineMessageModel
import pl.edu.pg.eti.presentation.viewmodel.DrawingViewModel
import pl.edu.pg.eti.presentation.viewmodel.GuessingViewModel
import java.nio.charset.StandardCharsets
import kotlin.math.sin

@AndroidEntryPoint
class GuessingFragment : Fragment() {

    private lateinit var binding: FragmentGuessingBinding
    private lateinit var bitmap: Bitmap
    private lateinit var canvas: Canvas
    private var paint = Paint()
    private val viewModel: GuessingViewModel by viewModels()

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
        paint.setColor(Color.BLACK)
        paint.setAntiAlias(true)
        paint.setStyle(Paint.Style.STROKE)
        paint.strokeCap = Paint.Cap.ROUND
        paint.setStrokeWidth(10f)
        viewModel.createServerConnection(deliverCallback)
    }


    private fun drawPainting(
        floatStartX: Float,
        floatStartY: Float,
        floatEndX: Float,
        floatEndY: Float,
        paint: Paint
    ) {
        val paintttt = Paint()
        paintttt.apply {
            color = Color.RED
            isAntiAlias = true
            style = Paint.Style.STROKE
            strokeCap = Paint.Cap.ROUND
            setStrokeWidth(10f)
        }
        canvas.drawLine(floatStartX, floatStartY, floatEndX, floatEndY, paintttt)
        binding.imageView.setImageBitmap(bitmap)
    }

    fun menageMessage(message: String): CanvaSingleLineMessageModel {
        val array = message.split(",")

        return CanvaSingleLineMessageModel(
            array[0].toFloat(), array[1].toFloat(), array[2].toFloat(), array[3].toFloat(),
            Paint()
        )
    }

    val deliverCallback = DeliverCallback { consumerTag: String?, delivery: Delivery ->
        requireActivity().runOnUiThread {
            val message = String(delivery.body, StandardCharsets.UTF_8)
            val singleLineMessageModel = menageMessage(message)
            paint = Paint()
            drawPainting(
                singleLineMessageModel.startX,
                singleLineMessageModel.startY,
                singleLineMessageModel.endX,
                singleLineMessageModel.endY,
                paint
            )

            println("[$consumerTag] Received: '$message'")
        }


    }


}