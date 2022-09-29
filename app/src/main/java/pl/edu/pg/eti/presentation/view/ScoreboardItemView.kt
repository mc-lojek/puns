package pl.edu.pg.eti.presentation.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import androidx.databinding.DataBindingUtil
import pl.edu.pg.eti.R
import pl.edu.pg.eti.databinding.ViewScoreboardItemBinding
import pl.edu.pg.eti.domain.model.ScoreboardItemModel

class ScoreboardItemView : FrameLayout {
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

    lateinit var binding: ViewScoreboardItemBinding

    private fun init() {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        binding = DataBindingUtil.inflate(inflater, R.layout.view_scoreboard_item, this, true)
    }

    private fun parseAttrs(attrs: AttributeSet) {

    }

    fun bind(item: ScoreboardItemModel) {
        binding.scoreboardRow=item
    }

    fun setVisibility(isVisible: Boolean) {
        if (isVisible) {
            binding.ivPlace.visibility = View.VISIBLE
        } else {
            binding.ivPlace.visibility = View.INVISIBLE
        }
    }


}