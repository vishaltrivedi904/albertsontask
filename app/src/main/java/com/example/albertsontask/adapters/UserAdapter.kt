package com.example.albertsontask.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.albertsontask.R
import com.example.albertsontask.calbacks.OnItemClickListener
import com.example.albertsontask.data.model.user.Result
import com.example.albertsontask.databinding.UserItemBinding

class UserAdapter(
    private val context: Context,
    private var data: MutableList<Result>,
    private val listener: OnItemClickListener<Result>
) :
    RecyclerView.Adapter<UserAdapter.ActionViewHolder?>() {

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ActionViewHolder {
        return ActionViewHolder(
            UserItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ActionViewHolder, position: Int) {
        val user = data[position]

        Glide.with(context).load(user.picture?.large)
            .placeholder(R.drawable.ic_placeholder).into(holder.binding.userImage)

        holder.binding.userName.text =
            "${data[position].name?.title} ${user.name?.first} ${user.name?.last}"

        user.location?.let {
            holder.binding.userAddress.text = "${it.street.number} ${it.street.name}, " +
                    "${it.city}, ${it.state}, " +
                    "${it.country}, ${it.postcode}"
        }

        holder.itemView.setOnClickListener {
            listener.onItemClicked(data[position])
        }
    }


    fun setItems(list: MutableList<Result>) {
        var startPosition = 0
        data.apply {
            addAll(list)
            startPosition = data.size
        }
        notifyItemRangeInserted(startPosition, list.size)
    }

    fun clear() {
        val size = data.size
        if (size > 0) {
            data.clear()
            notifyItemRangeRemoved(0, size)
        }
    }

    class ActionViewHolder(binding: UserItemBinding?) :
        RecyclerView.ViewHolder(binding!!.root) {
        var binding: UserItemBinding

        init {
            this.binding = binding!!
        }
    }

}