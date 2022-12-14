package pl.edu.pg.eti.presentation.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.player_list_element.view.*
import pl.edu.pg.eti.R
import pl.edu.pg.eti.domain.model.PlayerListClass


class PlayerListAdapter(
    private val playerList: MutableList<PlayerListClass>,
) : RecyclerView.Adapter<PlayerListAdapter.ViewHolder>() {

    class ViewHolder(val v: View) :
        RecyclerView.ViewHolder(v) {
        fun bind(elem: PlayerListClass, position: Int) {

            if(position % 2 == 1) {
                v.setBackgroundColor(Color.parseColor("#EDB007"))
            } else {
                v.setBackgroundColor(Color.parseColor("#F9C22E"))
            }

            v.tv_nickname.text = elem.nickname
        }
    }

    fun addElem(elem: PlayerListClass){
        //Timber.d("Dodawanie do recyclera gracza ${elem.nickname}")
        this.playerList.add(0,elem)
        notifyItemInserted(0)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val viewHolder =
            LayoutInflater.from(parent.context).inflate(R.layout.player_list_element, parent, false)
        return ViewHolder(viewHolder)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val elem = playerList.get(position)
        holder.bind(elem, position)
    }

    override fun getItemCount(): Int {
        return playerList.size
    }

    fun clearAll(){
        val size = playerList.size
        this.playerList.clear()
        notifyDataSetChanged()
        //notifyItemRangeChanged(0,size-1)
    }
}