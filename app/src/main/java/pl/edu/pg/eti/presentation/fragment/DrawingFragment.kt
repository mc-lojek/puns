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
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import pl.edu.pg.eti.R
import pl.edu.pg.eti.databinding.FragmentDrawingBinding
import pl.edu.pg.eti.domain.model.MessageModel
import pl.edu.pg.eti.presentation.adapter.MessageRecyclerViewAdapter
import pl.edu.pg.eti.presentation.viewmodel.DrawingViewModel


class DrawingFragment : Fragment() {

    private lateinit var binding: FragmentDrawingBinding
    private var floatStartX = -1f
    private var floatStartY = -1f
    private var floatEndX = -1f
    private var floatEndY = -1f
    private lateinit var bitmap: Bitmap
    private lateinit var canvas: Canvas
    private var paint = Paint()
    private var isSizeSet = false
    private var adapter = MessageRecyclerViewAdapter(arrayListOf())
    private lateinit var linearLayoutManager: LinearLayoutManager

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
        setupDrawingListener()
        setupAdapter()
    }

    private fun setupAdapter(){
        binding.chatRecyclerView.layoutManager = LinearLayoutManager(context)
        binding.chatRecyclerView.adapter=adapter
    }

    private fun setStartingValues()
    {
        if(!isSizeSet){
            bitmap = Bitmap.createBitmap(binding.imageView.width, binding.imageView.width, Bitmap.Config.ARGB_8888)
            canvas = Canvas(bitmap)
            paint.setColor(Color.BLACK)
            paint.setAntiAlias(true)
            paint.setStyle(Paint.Style.STROKE)
            paint.strokeCap=Paint.Cap.ROUND
            paint.setStrokeWidth(10f)
            isSizeSet=true
        }
    }

    private fun onPaintButtonClick(view: View) {
        setStartingValues()
        paint.color = view.backgroundTintList!!.defaultColor
    }

    private fun setupDrawingListener() {
        binding.imageView.setOnTouchListener(object : View.OnTouchListener {
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


        fun onPaintButtonClick(view: View) {
            setStartingValues()
            paint.color = view.backgroundTintList!!.defaultColor
        }

        binding.btnBlack.setOnClickListener(::onPaintButtonClick)
        binding.btnBlue.setOnClickListener(::onPaintButtonClick)
        binding.btnGreen.setOnClickListener(::onPaintButtonClick)
        binding.btnRed.setOnClickListener(::onPaintButtonClick)

        binding.btnPlus.setOnClickListener {
            setStartingValues()
            paint.strokeWidth = if(paint.strokeWidth+2f>20f) 20f else paint.strokeWidth+2f
            //todo wrzucić do constów max i min szerokość pędzli, jeśli nie zmienimy sposobu zmiany rozmiaru tego pędzla

        }
        binding.btnMinus.setOnClickListener {
            setStartingValues()
            paint.strokeWidth = if(paint.strokeWidth-2f<6) 6f else paint.strokeWidth-2f
            adapter.addMessage(MessageModel("elo","content"))
            //todo wrzucić do constów max i min szerokość pędzli, jeśli nie zmienimy sposobu zmiany rozmiaru tego pędzla
        }
    }


    private fun setKeyWord(keyword:String){
        binding.tvKeyword.text = "${resources.getString(R.string.keyword_prefix)}: ${keyword}"
    }

    private fun setTimeLeft(timeInSeconds:Int){
        binding.tvTimeLeft.text=timeInSeconds.toString()+"s"
    }

    private fun setRoundTV(now:Int,total:Int){
        binding.tvRound.text=now.toString()+"/"+total.toString()
    }

    private fun drawPainting() {
        canvas.drawLine(floatStartX,floatStartY,floatEndX,floatEndY,paint)
        binding.imageView.setImageBitmap(bitmap)
    }
}