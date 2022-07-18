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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import pl.edu.pg.eti.R
import pl.edu.pg.eti.databinding.FragmentDrawingBinding
import pl.edu.pg.eti.domain.model.MessageModel
import pl.edu.pg.eti.presentation.adapter.MessageRecyclerViewAdapter
import pl.edu.pg.eti.presentation.viewmodel.DrawingViewModel


class DrawingFragment : Fragment() {

    private lateinit var binding: FragmentDrawingBinding
    //to do wyjebania, wystarczy ze dasz binding.imageView zamiast imageView
    private lateinit var imageView: ImageView
    private var floatStartX = -1f
    private var floatStartY = -1f
    private var floatEndX = -1f
    private var floatEndY = -1f
    private lateinit var bitmap: Bitmap
    private lateinit var canvas: Canvas
    //to mozna od razu zainicjaizowac
    //private val paint = Paint()
    private lateinit var paint: Paint
    private var isSizeSet = false
    //adapter mozna zainicjalizowac od razu
    //private val adapter = MessageRecyclerViewAdapter()
    private lateinit var adapter: MessageRecyclerViewAdapter
    //to do wyjebania
    private lateinit var recycler: RecyclerView
    private lateinit var linearLayoutManager: LinearLayoutManager

    //to do wyjebania
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
        //to lepiej dac do onViewCreated()
        setupAdapter()
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //te dwie linijki do wyjebania
        imageView = binding.imageView
        paint = Paint()
        setupDrawingListener()
    }

    private fun setupAdapter(){

        //tutaj po moich sugestiach z gory zostaloby tylko tyle:
        //binding.chatRecyclerView.layoutManager = LinearLayoutManager(context) //i tutaj dawaj context zamiast activity
        //binding.chatRecyclerView.adapter=adapter

        recycler = binding.chatRecyclerView
        linearLayoutManager = LinearLayoutManager(this.activity)
        recycler.layoutManager = linearLayoutManager
        adapter= MessageRecyclerViewAdapter(arrayListOf())
        recycler.adapter=adapter
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

    private fun onPaintButtonClick(view: View) {
        setStartingValues()
        paint.color = view.backgroundTintList!!.defaultColor
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

        //tutaj mozesz dac jeden listener wspolny dla wszyskich przyciskow
//        binding.btnBlack.setOnClickListener(::onPaintButtonClick)
//        binding.btnBlue.setOnClickListener(::onPaintButtonClick)
//        binding.btnGreen.setOnClickListener(::onPaintButtonClick)
//        binding.btnRed.setOnClickListener(::onPaintButtonClick)


        binding.btnBlack.setOnClickListener{
            setStartingValues()
            paint.setColor(Color.parseColor("#000000"))
        }
        binding.btnBlue.setOnClickListener {
            setStartingValues()
            paint.setColor(Color.parseColor("#03A9F4"))
        }
        binding.btnGreen.setOnClickListener {
            binding.btnGreen.backgroundTintList.defaultColor
            setStartingValues()
            paint.setColor(Color.parseColor("#4CAF50"))
        }
        binding.btnRed.setOnClickListener {
            setStartingValues()
            paint.setColor(Color.parseColor("#F44336"))
        }
        // ogolnie w kotlinie zamiast setterow zazwyczaj jest property syntax
        // wiec tak jak ide podpowiada mozesz sobie zrobic
        // paint.strokeWidth = xxx zamiast paint.setStrokeWidth(xxx)
        binding.btnPlus.setOnClickListener {
            setStartingValues()
            paint.setStrokeWidth(if(paint.strokeWidth+2f>20f) 20f else paint.strokeWidth+2f)
            //todo wrzucić do constów max i min szerokość pędzli, jeśli nie zmienimy sposobu zmiany rozmiaru tego pędzla

        }
        binding.btnMinus.setOnClickListener {
            setStartingValues()
            paint.setStrokeWidth(if(paint.strokeWidth-2f<6) 6f else paint.strokeWidth-2f)
            adapter.addMessage(MessageModel("elo","content"))
            //todo wrzucić do constów max i min szerokość pędzli, jeśli nie zmienimy sposobu zmiany rozmiaru tego pędzla
        }
    }

    private fun setKeyWord(keyword:String){
        binding.tvKeyword.text="Hasło: "+keyword
        //todo dodać "Hasło" do constów jakbyśmy chcieli robić wersje językowe

        // ^ tu ogolnie takie wartosci tekstowe to trzymamy w strings.xml
        // i wtedy mozna dla kazdej wersji jezykowej inny plik przygotowac z tymi samymi slowami
        // w xmlu sie ich uzywa jako @string/<key>
        // a w kodzie tak:
        // resources.getString(R.id.<key>)
        // a zamiast sklejać stringi przez operator + to mozesz ten formatowany string robić
        // binding.tvKeyword.text = "${resources.getString(R.id.keyword)}: ${keyword}"
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


    // co do xml to musimy przyjac jakas konwencje co do nazywania plikow i nadawania id, zeby sie nie zrobil rozpierdol
    // znajde jakas sensowna rozpiske na necie i podrzuce wam linka zebyscie sie nia tym wzorowali.
    // no i stringi deklaruj w strings.xml a kolory w colors.xml i wtedy zamiast podawac bezposrednio
    // np.#ff0000 to dajesz @color/red, bedzie mozna pozniej przyjemnie zmieniac odcienie itp

}