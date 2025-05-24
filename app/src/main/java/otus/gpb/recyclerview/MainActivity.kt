package otus.gpb.recyclerview

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import otus.gpb.recyclerview.data.ChatItem
import otus.gpb.recyclerview.data.GroupChat
import otus.gpb.recyclerview.data.PersonChat
import otus.gpb.recyclerview.data.groupChatList
import otus.gpb.recyclerview.data.personChatList
import otus.gpb.recyclerview.databinding.ActivityMainBinding
import otus.gpb.recyclerview.ui.ChatAdapter
import otus.gpb.recyclerview.ui.CustomDecorator
import kotlin.random.Random
import androidx.recyclerview.widget.RecyclerView.OnScrollListener
import otus.gpb.recyclerview.ui.ItemTouchHelperCallback


class MainActivity : AppCompatActivity() {

    private var chatItems: List<ChatItem> = emptyList()

    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: ChatAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.recyclerView.addItemDecoration(CustomDecorator(this).apply {
            setColor(R.color.color_chat_divider_light)
            setOffset(R.integer.dividerOffset)
        })

        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = ChatAdapter()

        binding.recyclerView.adapter = adapter

        chatItems = generateList()
        adapter.submitList(chatItems)

        binding.recyclerView.addOnScrollListener(object : OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                val totalItemCount = layoutManager.itemCount
                val lastVisible = layoutManager.findLastVisibleItemPosition()

                if (lastVisible > totalItemCount - 4) {
                    chatItems += generateList(chatItems.size)
                    adapter.submitList(chatItems)
                }
            }
        })

        val itemTouchCallback = ItemTouchHelperCallback(::getChatItems,::setChatItems).apply {
            setSwipeEdgeParams(
                resources.getInteger(R.integer.swipeEdgeCornerRadius),
                resources.getColor(R.color.color_primary_light),
                resources.getColor(R.color.color_chat_background_on_swipe_light)
                )
            ResourcesCompat.getDrawable(resources, R.drawable.icon_archive, theme)
                ?.let { setArchIcon(it, 20, 20, 20,
                    resources.getString(R.string.arch_icon_text)) }
        }
        ItemTouchHelper(itemTouchCallback).attachToRecyclerView(binding.recyclerView)
    }

    private fun getChatItems() = chatItems
    private fun setChatItems(list: List<ChatItem>) {
        chatItems = list
        adapter.submitList(list)
    }

    private fun getRandomBool() = Random.nextInt(0, 2) > 0

    @SuppressLint("DefaultLocale")
    private fun getRandomTime() : String {
        val hour = Random.nextInt(0, 24)
        val min = Random.nextInt(0, 60)
        return String.format("%d:%02d", hour, min)
    }

    private fun randomizeGroupChat(item: GroupChat, offset: Int) : GroupChat {
        return GroupChat(
            id = item.id + offset,
            groupName = item.groupName,
            lastAuthor = item.lastAuthor,
            lastMessage = item.lastMessage,
            avatarUrl = item.avatarUrl,
            messagePreviewUrl = item.messagePreviewUrl,
            voip = getRandomBool(),
            verified = getRandomBool(),
            muted = getRandomBool(),
            time = getRandomTime(),
            checked = getRandomBool(),
            read = getRandomBool(),
            mentioned = getRandomBool(),
            pinned = getRandomBool(),
            counter = if (Random.nextInt(0, 5) > 0) 0 else {
                Random.nextInt(1, 199)
            }
        )
    }

    private fun randomizePersonChat(item: PersonChat, offset: Int) : PersonChat {
        return PersonChat(
            id = item.id + offset,
            personName = item.personName,
            lastMessage = item.lastMessage,
            avatarUrl = item.avatarUrl,
            messagePreviewUrl = item.messagePreviewUrl,
            checkbox = getRandomBool(),
            online = getRandomBool(),
            locked = getRandomBool(),
            scam = if (Random.nextInt(0, 5) > 0) false else getRandomBool(),
            verified = getRandomBool(),
            muted = getRandomBool(),
            time = getRandomTime(),
            checked = getRandomBool(),
            read = getRandomBool(),
            mentioned = getRandomBool(),
            pinned = getRandomBool(),
            counter = if (Random.nextInt(0, 5) > 0) 0 else {
                Random.nextInt(1, 19)
            }
        )
    }

    private fun generateList(startId: Int = 0) : List<ChatItem> {
        if (startId == 0) {
            return listOf(
                personChatList[0],
                groupChatList[0],
                personChatList[1],
                groupChatList[1],
                groupChatList[2],
                personChatList[2],
                groupChatList[3],
                personChatList[3]
            )
        }
        else {
            return listOf(
                randomizePersonChat(personChatList[0], startId),
                randomizeGroupChat(groupChatList[0], startId),
                randomizePersonChat(personChatList[1], startId),
                randomizeGroupChat(groupChatList[1], startId),
                randomizeGroupChat(groupChatList[2], startId),
                randomizePersonChat(personChatList[2], startId),
                randomizeGroupChat(groupChatList[3], startId),
                randomizePersonChat(personChatList[3], startId)
            )
        }
    }
}

