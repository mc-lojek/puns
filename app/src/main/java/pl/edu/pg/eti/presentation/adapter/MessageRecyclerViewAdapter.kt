package pl.edu.pg.eti.presentation.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import pl.edu.pg.eti.R
import pl.edu.pg.eti.domain.model.events.ChatMessageEvent
import pl.edu.pg.eti.domain.model.events.PlayerHitEvent
import timber.log.Timber

class MessageRecyclerViewAdapter(
    private var messageList: MutableList<ChatMessageEvent>
):RecyclerView.Adapter<MessageRecyclerViewAdapter.ViewHolder>(){


    fun addMessage(message: ChatMessageEvent){
        Timber.d("${message.nickname} ${message.content}")
        this.messageList.add(0,message)
        notifyItemInserted(0)
    }

    fun addMessage(message: PlayerHitEvent){
        Timber.d("Odgadniete haslo przez ${message.nickname}")
        this.messageList.add(0,ChatMessageEvent(message.nickname,"${message.nickname} guessed right!",3))
        notifyItemInserted(0)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val viewHolder = LayoutInflater.from(parent.context).inflate(R.layout.chat_recycler_view_message, parent, false)
        return ViewHolder(viewHolder)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val message = messageList.get(position)
        holder.bind(message)
    }

    override fun getItemCount(): Int {
        return messageList.size
    }


    class ViewHolder(val v: View):RecyclerView.ViewHolder(v){
        fun bind(message: ChatMessageEvent){
            v.findViewById<TextView>(R.id.tvNickname).text=message.nickname
            v.findViewById<TextView>(R.id.tvContent).text=message.content
            //todo message.level obsługiwanie
        }
    }
}