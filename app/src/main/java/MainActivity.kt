package com.example.newsapplication
import Article
import OnArticlieClickListener
import RecyclerAdapter
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

const val BASE_URL = "https://api.currentsapi.services"

class MainActivity : AppCompatActivity() ,OnArticlieClickListener{

    lateinit var countdownTimer: CountDownTimer
    private var seconds = 3L

    private var titlesList = mutableListOf<String>()
    private var descList = mutableListOf<String>()
    private var imagesList = mutableListOf<String>()
    private var linksList = mutableListOf<String>()
    private var myDataArticle :Article? = null
    private var myDataArticleList :ArrayList<Article?>? = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        makeAPIRequest()
    }


    //simple fade in animation for when the app is done loading
    lateinit var view: View
    private fun fadeIn() {
        view=findViewById(R.id.v_blackScreen)
        view.animate().apply {
            alpha(0f)
            duration = 3000
        }.start()
    }

    //requests data from the api and forwards it to the recycler view
    lateinit var progressBar:ProgressBar
    private fun makeAPIRequest() {
        progressBar=findViewById(R.id.progressBar)
        progressBar.visibility = View.VISIBLE

        val api = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(APIRequest::class.java)

        GlobalScope.launch(Dispatchers.IO) {
            try {
                val response = api.getNews()

                for (article in response.news) {
                    Log.d("MainActivity", "Result + $article")
                    myDataArticle =  Article(article.title, article.description, article.url,article.image)
                    myDataArticleList?.add(myDataArticle)
                }

                //updates ui when data has been retrieved
                GlobalScope.launch(Dispatchers.Main) {
                    setUpRecyclerView()
                    fadeIn()
                    progressBar.visibility = View.GONE
                }
            } catch (e: Exception) {
                Log.d("MainActivity", e.toString())
                withContext(Dispatchers.Main) {
                    attemptRequestAgain(seconds)

                }
            }

        }
    }

    lateinit var text: TextView
    private fun attemptRequestAgain(seconds: Long) {
        countdownTimer = object: CountDownTimer(seconds*1010,1000){
            override fun onFinish() {
                makeAPIRequest()
                countdownTimer.cancel()
                text=findViewById(R.id.tv_noInternetCountDown)
                text.visibility = View.GONE
                this@MainActivity.seconds+=3
            }
            override fun onTick(millisUntilFinished: Long) {
                text.visibility = View.VISIBLE
                text.text = "Cannot retrieve data...\nTrying again in: ${millisUntilFinished/1000}"
                Log.d("MainActivity", "Could not retrieve data. Trying again in ${millisUntilFinished/1000} seconds")
            }
        }
        countdownTimer.start()
    }

    lateinit var recyclerView: RecyclerView

    private fun setUpRecyclerView() {
        recyclerView=findViewById(R.id.rv_rv)
        recyclerView.layoutManager = LinearLayoutManager(applicationContext)
        recyclerView.adapter = myDataArticleList?.let {
            RecyclerAdapter(it, this)
        }

//        var myNameClass = MyNameClass(name = "ahmed")
//        myNameClass.apply {
//            this.name = "My New Name"
//            this.age = "10"
//        }
        // let  apply  run
    }

    data class MyNameClass  (var name :String,var age :String = "10")
    //adds the items to our recyclerview
    private fun addToList(title: String, description: String, image: String, link: String) {
        linksList.add(link)
        titlesList.add(title)
        descList.add(description)
        imagesList.add(image)
    }

    override fun itemClick(myArticle: Article) {
        Toast.makeText(this,myArticle.title + myArticle.details,Toast.LENGTH_LONG).show()
    }

    override fun imageClick(myArticle: Article) {
                val intent = Intent(Intent.ACTION_VIEW)
                intent.data = Uri.parse(myArticle.link)
                startActivity( intent)
    }

}