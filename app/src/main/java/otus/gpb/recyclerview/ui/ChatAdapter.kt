package otus.gpb.recyclerview.ui

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import otus.gpb.recyclerview.data.ChatItem
import otus.gpb.recyclerview.R
import otus.gpb.recyclerview.data.GroupChat
import otus.gpb.recyclerview.data.PersonChat
import otus.gpb.recyclerview.databinding.VhGroupItemBinding
import otus.gpb.recyclerview.databinding.VhPersonItemBinding

class ChatAdapter(private val onItemClick: (Int) -> Unit):
    ListAdapter<ChatItem, RecyclerView.ViewHolder>(ChatDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            ViewTypes.GROUP.id-> {
                val binding =
                    VhGroupItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                GroupChatViewHolder(binding)
            }

            ViewTypes.PERSON.id-> {
                val binding =
                    VhPersonItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                PersonChatViewHolder(binding)
            }

            else -> throw IllegalArgumentException(parent.context.getString(R.string.unknown_view_type))
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is GroupChat -> ViewTypes.GROUP.id //R.layout.vh_group_item
            is PersonChat -> ViewTypes.PERSON.id //R.layout.vh_person_item
            else -> -1
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (val item = getItem(position)/*val item = data[position]*/) {
            is GroupChat -> (holder as GroupChatViewHolder).bind(item)
            is PersonChat -> (holder as PersonChatViewHolder).bind(item)
        }
    }

    inner class GroupChatViewHolder(private val binding: VhGroupItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: GroupChat) {

            if (item.avatarUrl.isNotEmpty()) {
                Glide.with(binding.root.context)
                    .load(item.avatarUrl)
                    .centerCrop()
                    .into(binding.imgGroupAvatar)
            } else binding.imgGroupAvatar.setImageResource(R.drawable.supervised_user_circle_24px)

            binding.imgVoipBadgeContainer.visibility = if (item.voip) View.VISIBLE
            else View.GONE

            binding.txtGroupName.text = item.groupName

            binding.imgVerifiedIcon.visibility = if (item.verified) View.VISIBLE
            else View.GONE

            binding.imgMuteIcon.visibility = if (item.muted) View.VISIBLE
            else View.GONE

            binding.txtLastAuthor.text = item.lastAuthor

            if (item.messagePreviewUrl.isNotEmpty()) {
                binding.imgMessagePreviewContainer.visibility = View.VISIBLE
                Glide.with(binding.root.context)
                    .load(item.messagePreviewUrl)
                    .centerCrop()
                    .into(binding.imgMessagePreview)
            } else binding.imgMessagePreviewContainer.visibility = View.GONE

            binding.txtLastMessage.text = item.lastMessage
            binding.txtTimeValue.text = item.time

            binding.imgCheckedIcon.visibility = if (item.checked && !item.read) View.VISIBLE
            else View.GONE

            binding.imgReadIcon.visibility = if (item.read) View.VISIBLE
            else View.GONE

            if (item.counter == 0) binding.imgCounterContainer.visibility = View.GONE
            else {
                binding.imgCounterContainer.visibility = View.VISIBLE
                binding.txtCounterContainer.text = item.counter.toString()
            }

            binding.imgPinnedIcon.visibility = if (item.pinned) View.VISIBLE
            else View.GONE

            binding.imgMentionIconContainer.visibility = if (item.mentioned) View.VISIBLE
            else View.GONE

            binding.root.setOnClickListener { onItemClick(item.id) }
        }
    }

    inner class PersonChatViewHolder(private val binding: VhPersonItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: PersonChat) {

            if (item.avatarUrl.isNotEmpty()) {
                Glide.with(binding.root.context)
                    .load(item.avatarUrl)
                    .centerCrop()
                    .into(binding.imgPersonAvatar)
            } else binding.imgPersonAvatar.setImageResource(R.drawable.account_circle_24px)

            binding.imgCheckboxBadgeContainer.visibility = if (item.checkbox && !item.online) View.VISIBLE
            else View.GONE

            binding.imgOnlineBadgeContainer.visibility = if (item.online) View.VISIBLE
            else View.GONE

            binding.imgLockedIcon.visibility = if (item.locked) View.VISIBLE
            else View.GONE

            binding.txtPersonName.text = item.personName

            binding.imgScamPatch.visibility = if (item.scam) View.VISIBLE
            else View.GONE

            binding.imgVerifiedIcon.visibility = if (item.verified && !item.scam) View.VISIBLE
            else View.GONE

            binding.imgMuteIcon.visibility = if (item.muted) View.VISIBLE
            else View.GONE

            if (item.messagePreviewUrl.isNotEmpty()) {
                binding.imgMessagePreviewContainer.visibility = View.VISIBLE
                Glide.with(binding.root.context)
                    .load(item.messagePreviewUrl)
                    .centerCrop()
                    .into(binding.imgMessagePreview)
            } else binding.imgMessagePreviewContainer.visibility = View.GONE

            binding.txtLastMessage.text = item.lastMessage
            binding.txtTimeValue.text = item.time

            binding.imgCheckedIcon.visibility = if (item.checked && !item.read) View.VISIBLE
            else View.GONE

            binding.imgReadIcon.visibility = if (item.read) View.VISIBLE
            else View.GONE

            if (item.counter == 0) binding.imgCounterContainer.visibility = View.GONE
            else {
                binding.imgCounterContainer.visibility = View.VISIBLE
                binding.txtCounterContainer.text = item.counter.toString()
            }

            binding.imgPinnedIcon.visibility = if (item.pinned) View.VISIBLE
            else View.GONE

            binding.imgMentionIconContainer.visibility = if (item.mentioned) View.VISIBLE
            else View.GONE

            binding.root.setOnClickListener { onItemClick(item.id) }
        }
    }

    enum class ViewTypes(val id: Int) {
        GROUP(R.layout.vh_group_item),
        PERSON(R.layout.vh_person_item)
    }
}

class ChatDiffCallback : DiffUtil.ItemCallback<ChatItem>() {
    override fun areItemsTheSame(oldItem: ChatItem, newItem: ChatItem): Boolean {
        return when {
            oldItem is GroupChat && newItem is GroupChat -> oldItem.id == newItem.id
            oldItem is PersonChat && newItem is PersonChat -> oldItem.id == newItem.id
            else -> false
        }
    }

    @SuppressLint("DiffUtilEquals")
    override fun areContentsTheSame(oldItem: ChatItem, newItem: ChatItem): Boolean {
        return oldItem == newItem
    }
}