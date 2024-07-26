//package com.example.tk3
//
//import android.content.Context
//import android.content.Intent
//import android.os.Bundle
//import android.widget.Button
//import android.widget.TextView
//import androidx.appcompat.app.AppCompatActivity
//import com.example.tk3.adapter.DestinationListAdapter
//import com.example.tk3.database.DatabaseHelper
//import com.example.tk3.databinding.ActivityMainBinding
//import com.example.tk3.model.DestinationListModel
//import com.google.android.gms.auth.api.signin.GoogleSignIn
//import com.google.android.gms.auth.api.signin.GoogleSignInClient
//import com.google.android.gms.auth.api.signin.GoogleSignInOptions
//import com.google.firebase.auth.FirebaseAuth
//import com.google.firebase.auth.ktx.auth
//import com.google.firebase.ktx.Firebase
//import com.google.firebase.firestore.FirebaseFirestore
//
//class MainActivity2 : AppCompatActivity() {
//
//    private lateinit var mGoogleSignInClient: GoogleSignInClient
//    private lateinit var mAuth: FirebaseAuth
//    var destinationlistAdapter: DestinationListAdapter ?= null
//    var dbHandler: DatabaseHelper ?= null
//    var destinationlist: List<DestinationListModel> = ArrayList<DestinationListModel>()
//    private lateinit var firestore: FirebaseFirestore
//    private lateinit var binding: ActivityMainBinding
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        binding = ActivityMainBinding.inflate(layoutInflater)
//        setContentView(binding.root)
//        firestore = FirebaseFirestore.getInstance()
//        mAuth = FirebaseAuth.getInstance()
//        val currentUser = mAuth.currentUser
//        if (currentUser == null) {
//            val intent = Intent(this, SignInActivity::class.java)
//            startActivity(intent)
//            finish()
//            return
//        }
//
//        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
//            .requestIdToken(getString(R.string.default_web_client_id))
//            .requestEmail()
//            .build()
//
//        mGoogleSignInClient = GoogleSignIn.getClient(this, gso)
//
//        val textView = findViewById<TextView>(R.id.name)
//
//        dbHandler = DatabaseHelper(this)
//        fetchlist()
//
//        val auth = Firebase.auth
//        val user = auth.currentUser
//
//        if (user != null) {
//            val userName = user.displayName
//            textView.text = "Welcome, " + userName
//        }
//
//        binding.btAddItems.setOnClickListener {
//            MapsActivity.open(context = this@MainActivity, mode = MapsActivity.NEW_DATA)
//        }
//
//        val sign_out_button = findViewById<Button>(R.id.logout_button)
//        sign_out_button.setOnClickListener {
//            signOutAndStartSignInActivity()
//        }
//    }
//
//    private fun fetchlist() {
//        destinationlist = dbHandler!!.getAllDestinations()
//        destinationlistAdapter = DestinationListAdapter(destinationlist, this@MainActivity)
//        binding.rvList.adapter = destinationlistAdapter
//        destinationlistAdapter?.notifyDataSetChanged()
//    }
//
//    private fun signOutAndStartSignInActivity() {
//        mAuth.signOut()
//
//        mGoogleSignInClient.signOut().addOnCompleteListener(this) {
//            // Optional: Update UI or show a message to the user
//            val intent = Intent(this@MainActivity, SignInActivity::class.java)
//            startActivity(intent)
//            finish()
//        }
//    }
//
//    companion object{
//
//        fun open(context: Context){
//            context.startActivity(Intent(context, MainActivity::class.java))
//        }
//    }
//}