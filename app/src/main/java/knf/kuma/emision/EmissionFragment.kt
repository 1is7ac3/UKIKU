package knf.kuma.emision

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import knf.kuma.R
import knf.kuma.commons.PrefsUtil
import knf.kuma.commons.verifyManager
import knf.kuma.database.CacheDB
import knf.kuma.pojos.AnimeObject
import knf.kuma.widgets.emision.WEmisionProvider
import kotlinx.android.synthetic.main.recycler_emision.*
import org.jetbrains.anko.doAsync
import org.jsoup.Jsoup
import pl.droidsonroids.jspoon.Jspoon
import java.util.*

class EmissionFragment : Fragment() {
    private val dao = CacheDB.INSTANCE.animeDAO()
    private var adapter: EmissionAdapter? = null
    private var isFirst = true

    private val blacklist: Set<String>
        get() = if (PrefsUtil.emissionShowHidden)
            LinkedHashSet()
        else
            PrefsUtil.emissionBlacklist

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return LayoutInflater.from(context).inflate(R.layout.recycler_emision, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        adapter = EmissionAdapter(this)
        recycler.verifyManager()
        recycler.adapter = adapter
        if (context != null)
            CacheDB.INSTANCE.animeDAO().getByDay(arguments?.getInt("day", 1)
                    ?: 1, blacklist).observe(this, Observer { animeObjects ->
                progress.visibility = View.GONE
                    adapter?.update(animeObjects as MutableList<AnimeObject>, false) { smoothScroll() }
                if (isFirst) {
                    isFirst = false
                    recycler.scheduleLayoutAnimation()
                    checkStates(animeObjects)
                }
                error.visibility = if (animeObjects.isEmpty()) View.VISIBLE else View.GONE
            })
    }

    private fun smoothScroll() {
        //recycler.layoutManager?.smoothScrollToPosition(recycler,null,0)
    }

    private fun checkStates(animeObjects: MutableList<AnimeObject>) {
        doAsync {
            try {
                for (animeObject in animeObjects) {
                    try {
                        val document = Jsoup.connect(animeObject.link).cookie("device", "computer").get()
                        val animeObject1 = AnimeObject(animeObject.link, Jspoon.create().adapter(AnimeObject.WebInfo::class.java).fromHtml(document.outerHtml()))
                        if (animeObject1.state != "En emisión") {
                            dao.updateAnime(animeObject1)
                            WEmisionProvider.update(context)
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }

                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    internal fun reloadList() {
        if (context != null)
            CacheDB.INSTANCE.animeDAO().getByDay(arguments?.getInt("day", 1)
                    ?: 1, blacklist).observe(this, Observer { animeObjects ->
                error?.visibility = View.GONE
                if (animeObjects != null && animeObjects.isNotEmpty())
                    adapter?.update(animeObjects) { smoothScroll() }
                else
                    adapter?.update(ArrayList()) { smoothScroll() }
                if (animeObjects == null || animeObjects.isEmpty())
                    error?.visibility = View.VISIBLE
            })
    }

    companion object {

        operator fun get(day: AnimeObject.Day): EmissionFragment {
            val bundle = Bundle()
            bundle.putInt("day", day.value)
            val fragment = EmissionFragment()
            fragment.arguments = bundle
            return fragment
        }
    }
}
