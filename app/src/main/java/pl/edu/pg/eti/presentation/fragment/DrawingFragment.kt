package pl.edu.pg.eti.presentation.fragment

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import pl.edu.pg.eti.R
import pl.edu.pg.eti.databinding.FragmentDrawingBinding
import pl.edu.pg.eti.presentation.viewmodel.DrawingViewModel


class DrawingFragment : Fragment() {

    private lateinit var binding: FragmentDrawingBinding
    private lateinit var imageView: ImageView
    private var floatStartX = -1f
    private var floatStartY = -1f
    private var floatEndX = -1f
    private var floatEndY = -1f
    private lateinit var bitmap: Bitmap
    private lateinit var canvas: Canvas
    private lateinit var paint: Paint
    private var isSizeSet = false

    companion object {
        fun newInstance() = DrawingFragment()
    }

    private lateinit var viewModel: DrawingViewModel

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
        imageView = binding.imageView
        paint = Paint()
        setupDrawingListener()
    }

    private fun setStartingValues()
    {
        if(!isSizeSet){
            bitmap = Bitmap.createBitmap(imageView.width, imageView.width, Bitmap.Config.ARGB_8888)
            canvas = Canvas(bitmap)
            paint.setColor(Color.BLACK)
            paint.setAntiAlias(true)
            paint.setStyle(Paint.Style.STROKE)
            paint.strokeCap=Paint.Cap.ROUND
            paint.setStrokeWidth(10f)
            isSizeSet=true
        }
    }


    private fun setupDrawingListener() {
        imageView.setOnTouchListener(object : View.OnTouchListener {
            override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                when (event?.action) {
                    MotionEvent.ACTION_DOWN -> {
                        setStartingValues()
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
        binding.btnBlack.setOnClickListener{
            setStartingValues()
            paint.setColor(Color.parseColor("#000000"))
        }
        binding.btnBlue.setOnClickListener {
            setStartingValues()
            paint.setColor(Color.parseColor("#03A9F4"))
        }
        binding.btnGreen.setOnClickListener {
            setStartingValues()
            paint.setColor(Color.parseColor("#4CAF50"))
        }
        binding.btnRed.setOnClickListener {
            setStartingValues()
            paint.setColor(Color.parseColor("#F44336"))
        }
        binding.btnPlus.setOnClickListener {
            setStartingValues()
            paint.setStrokeWidth(if(paint.strokeWidth+2f>20f) 20f else paint.strokeWidth+2f)
            //todo wrzucić do constów max i min szerokość pędzli, jeśli nie zmienimy sposobu zmiany rozmiaru tego pędzla
        }
        binding.btnMinus.setOnClickListener {
            setStartingValues()
            paint.setStrokeWidth(if(paint.strokeWidth-2f<6) 6f else paint.strokeWidth-2f)
            //todo wrzucić do constów max i min szerokość pędzli, jeśli nie zmienimy sposobu zmiany rozmiaru tego pędzla
        }
    }

    private fun setKeyWord(keyword:String){
        binding.tvKeyword.text="Hasło: "+keyword
        //todo dodać "Hasło" do constów jakbyśmy chcieli robić wersje językowe
    }

    private fun setTimeLeft(timeInSeconds:Int){
        binding.tvTimeLeft.text=timeInSeconds.toString()+"s"
    }

    private fun setRoundTV(now:Int,total:Int){
        binding.tvRound.text=now.toString()+"/"+total.toString()
    }

    private fun drawPainting() {
        canvas.drawLine(floatStartX,floatStartY,floatEndX,floatEndY,paint);
        imageView.setImageBitmap(bitmap);
    }




}