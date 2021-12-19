package com.example.headsup

import android.graphics.Color
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.headsup.databinding.CelebRowBinding
import kotlin.random.Random


class RVAdapter(
    private val container: ArrayList<celebItem>
) :
    RecyclerView.Adapter<RVAdapter.CelebViewHolder>() {
    class CelebViewHolder(val binding: CelebRowBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CelebViewHolder {
        return CelebViewHolder(
            CelebRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: CelebViewHolder, position: Int) {
        val cards = container[position]
        val colors = listOf<String>("#9b5de5", "#f15bb5", "#fee440", "#00bbf9", "#00f5d4")
        val rand = Random.nextInt(0,colors.size)
        holder.binding.apply {
            tvTitle.text=cards.name
            tvHint1.text=cards.taboo1
            tvHint2.text=cards.taboo2
            tvHint3.text=cards.taboo3

            holder.itemView.setBackgroundColor(Color.parseColor(colors[rand]))

        }
    }

    override fun getItemCount(): Int = container.size
}
