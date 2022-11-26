package com.penchan.noodoeapi.ui.park

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.penchan.noodoeapi.databinding.ViewholderParkItemBinding
import com.penchan.noodoeapi.model.entity.ParkLotInfo

class ParkAdapter : RecyclerView.Adapter<ParkAdapter.ParkViewHolder>() {

    private var list: MutableList<ParkLotInfo> = mutableListOf()

    fun updateList(items: List<ParkLotInfo>) {
        items.forEach { this.list.add(it) }

        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ParkViewHolder {
        return ParkViewHolder(
            ViewholderParkItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false)
        )
    }

    override fun onBindViewHolder(holder: ParkViewHolder, position: Int) {
        holder.binding.textId.text = list[position].id
        holder.binding.textName.text = list[position].name
        holder.binding.textAddress.text = list[position].address
        holder.binding.textTotal.text = list[position].totalcar.toString()

        if (list[position].availablecar == -1) {
            holder.binding.textAvailable.text = "Unknown"
            holder.binding.textCharge.text = "Unknown"
            holder.binding.textIdle.text = "Unknown"
        } else {
            holder.binding.textAvailable.text = list[position].availablecar.toString()
            if (list[position].charging < 0 && list[position].freecharge < 0) {
                list[position].scoketStatusList?.let {
                    var charge = 0
                    var idle = 0
                    it.forEach {
                        if (it.spot_status.equals("充電中")) {
                            charge++
                        } else {
                            idle++
                        }
                    }
                    list[position].charging = charge
                    list[position].freecharge = idle
                }
            }
            holder.binding.textCharge.text = list[position].charging.toString()
            holder.binding.textIdle.text = list[position].freecharge.toString()
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    class ParkViewHolder(val binding: ViewholderParkItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

    }
}