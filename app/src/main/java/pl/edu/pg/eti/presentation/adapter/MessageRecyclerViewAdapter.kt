package pl.edu.pg.eti.presentation.adapter

import android.graphics.Color
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
        if(message.level==5L){
            Timber.d("Koniec, haslem bylo: ${message.content}")
            this.messageList.add(0,ChatMessageEvent(message.nickname,"End of round. Keyword was: ${message.content}",3))
            notifyItemInserted(0)
        }
        else{
            if(message.level==1L){
                this.messageList.add(0,ChatMessageEvent(message.nickname,"So close! ${message.content}",1))
            }
            else{
                Timber.d("${message.nickname} ${message.content}")
                this.messageList.add(0,message)
            }
            notifyItemInserted(0)
        }
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
            if(message.level==3L){
                v.findViewById<TextView>(R.id.tvContent).setTextColor(Color.parseColor("#00ff00"))
            }
            else if(message.level==1L){
                v.findViewById<TextView>(R.id.tvContent).setTextColor(Color.parseColor("#ffa500"))
            }
            //todo message.level obsługiwanie
        }
    }
}