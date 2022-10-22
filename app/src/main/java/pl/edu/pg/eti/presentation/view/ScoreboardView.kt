package pl.edu.pg.eti.presentation.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import androidx.core.view.forEach
import androidx.databinding.DataBindingUtil
import pl.edu.pg.eti.R
import pl.edu.pg.eti.databinding.ViewScoreboardBinding
import pl.edu.pg.eti.databinding.ViewScoreboardItemBinding
import pl.edu.pg.eti.domain.model.ScoreboardItemModel
import pl.edu.pg.eti.domain.model.events.ScoreboardRow

class ScoreboardView  : FrameLayout {
    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
        parseAttrs(attrs)
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        init()
        parseAttrs(attrs)
    }

    lateinit var binding: ViewScoreboardBinding


    private fun init() {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        binding = DataBindingUtil.inflate(inflater, R.layout.view_scoreboard, this, true)
        val scoreboardItem = ScoreboardItemModel("1st","nickname","czesc")

    }

    fun bind(scoreboardRowList: List<ScoreboardItemModel>){
        binding.container.removeAllViews()
        for (scoreboardRow in scoreboardRowList) {
            val row = ScoreboardItemView(context)
            row.binding.scoreboardRow=scoreboardRow
            if(scoreboardRow.place!="1"){
                row.binding.ivPlace.visibility=View.INVISIBLE
            }
            binding.container.addView(row)
        }
    }

    private fun parseAttrs(attrs: AttributeSet) {

    }
}