package com.example.firebaseone

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.location.GnssAntennaInfo.Listener
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView.LayoutManager
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObjects
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.rows.*
import kotlinx.android.synthetic.main.rows.view.*
import kotlinx.android.synthetic.main.updatenote.*
import kotlinx.android.synthetic.main.updatenote.btn_update_note
import kotlinx.android.synthetic.main.updatenote.view.*
import kotlinx.android.synthetic.main.updatenote.view.btn_update_note
import java.util.zip.Inflater

class MainActivity : AppCompatActivity() , iNotesAdapter{

    var count:Int=0
    //Initializing Database
    lateinit var db:FirebaseFirestore
    //Initializing reference to NotesModal class
    //lateinit var notes:ArrayList<NotesModel>
    lateinit var singleNote:NotesModel
    lateinit var madapter:NotesAdapter
    lateinit var arrNotes:ArrayList<NotesModel>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        db=Firebase.firestore
        recview.layoutManager=LinearLayoutManager(this)
        //ViewCompat.setNestedScrollingEnabled(recview, true);

        fetchArrofNotes()
    }



    //On click Save Button
    fun saveToFirestore(view: View) {

        val tit=edt_title.text.toString()
        val desc=edt_description.text.toString()
        edt_title.text.clear()
        edt_description.text.clear()
        if (arrNotes.size==0){
            count=0
        }
        count++
        val id=count

        if (tit.isNotEmpty() && desc.isNotEmpty()){


            //Adding to DataBase
            singleNote=NotesModel(tit,desc,id)
            db.collection("MyNotes").document("$count").set(singleNote).addOnSuccessListener {
                Toast.makeText(this,"Data Added",Toast.LENGTH_SHORT).show()
            }.addOnFailureListener {
                Toast.makeText(this,"$it",Toast.LENGTH_SHORT).show()
            }
        }
        else{
            Toast.makeText(this,"Please Enter Title and Description",Toast.LENGTH_SHORT).show()
        }
    }

    private fun fetchArrofNotes() {

        arrNotes = ArrayList<NotesModel>()
        //lateinit var arrNotes:ArrayList<NotesModel>
        db.collection("MyNotes").orderBy("id").addSnapshotListener { value, error ->
            if (error == null && value!=null) {

                Log.d("AAP", "fetchArrofNotes: $value")
                    arrNotes.clear()

                arrNotes=value.toObjects(NotesModel::class.java) as ArrayList<NotesModel>

                    madapter=NotesAdapter(this)
                    recview.adapter=madapter
                    madapter.updateNotes(arrNotes)

            }
            else if (error!=null && value==null){
                Toast.makeText(this,"$error ",Toast.LENGTH_LONG).show()
                Log.d("PAP", "fetchArrofNotes: $error")
            }

        }
    }

    override fun deleteNote(note: NotesModel) {
        val builder = AlertDialog.Builder(this@MainActivity)
        builder.setMessage("Are you sure you want to Delete?")
            .setCancelable(false)
            .setPositiveButton("Yes") { dialog, id ->
                // Delete selected note from database
                db.collection("MyNotes").document("${note.id}").delete()
            }
            .setNegativeButton("No") { dialog, id ->
                // Dismiss the dialog
                dialog.dismiss()
            }
        val alert = builder.create()
        alert.show()
    }

    override fun updateNote(note: NotesModel) {
        val mdialoglayout=LayoutInflater.from(this).inflate(R.layout.updatenote,null)
        val mbuilder = AlertDialog.Builder(this).setTitle("Update Note").setView(mdialoglayout)
        val mAlertDialog=mbuilder.show()
        mdialoglayout.btn_update_note.setOnClickListener {
            val tit=mdialoglayout.edt_update_title.text.toString()
            val des=mdialoglayout.edt_update_description.text.toString()
            if(tit.isEmpty() || des.isEmpty()){
                Toast.makeText(this,"Please Enter all Fields",Toast.LENGTH_SHORT).show()
            }
            else{
                val idd=note.id
                db.collection("MyNotes").document("${note.id}").update("description",des,"id",idd,"title",tit)
                mAlertDialog.dismiss()
            }
        }
    }

}