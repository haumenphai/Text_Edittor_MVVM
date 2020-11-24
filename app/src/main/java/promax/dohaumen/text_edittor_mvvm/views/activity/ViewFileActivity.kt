package promax.dohaumen.text_edittor_mvvm.views.activity

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import promax.dohaumen.text_edittor_mvvm.R
import promax.dohaumen.text_edittor_mvvm.databinding.ActivityViewFileBinding
import promax.dohaumen.text_edittor_mvvm.models.FileText
import promax.dohaumen.text_edittor_mvvm.viewmodel.ViewFileActivityViewModel
import promax.dohaumen.text_edittor_mvvm.views.dialog.DialogSettingEditView

class ViewFileActivity : AppCompatActivity() {
    lateinit var b: ActivityViewFileBinding
    lateinit var viewModel: ViewFileActivityViewModel

    private var readOnLy = false // readOnly ap dung voi mo file da bi xoa

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        b = ActivityViewFileBinding.inflate(layoutInflater)
        setContentView(b.root)

        val fileText: FileText? = intent.getParcelableExtra("fileText")
        if (intent.action == "Action view file read-only") {
            readOnLy = true
        }

        b.editViewFile.setText(fileText!!.content)

        viewModel = ViewModelProvider(this).get(ViewFileActivityViewModel::class.java)
        viewModel.fileText = fileText


        viewModel.isEditTextEnable().observeForever { isEditHometEnable ->
            b.editViewFile.isEnabled = isEditHometEnable

            if (isEditHometEnable) {
                menu?.findItem(R.id.menu_edit)?.setIcon(R.drawable.ic_edit)
                Toast.makeText(this, "editing...", Toast.LENGTH_SHORT).show()
            } else {
                menu?.findItem(R.id.menu_edit)?.setIcon(R.drawable.ic_edit_gray)
            }
        }
        viewModel.getTextSize().observeForever {
            b.editViewFile.textSize = it.toFloat()
        }

        setConfigToolBar()
    }

    fun setConfigToolBar() {
        setSupportActionBar(b.toolBar)
        b.appbarLayout.outlineProvider = null
        supportActionBar!!.title = viewModel.fileText.name

        b.toolBar.setNavigationOnClickListener {
            onBackPressed()
        }
    }





    var menu: Menu? = null
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.activity_view_file_menu, menu)
        this.menu = menu
        if (readOnLy) {
            menu?.findItem(R.id.menu_save)?.isVisible = false
            menu?.findItem(R.id.menu_edit)?.isVisible = false
        }
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_edit -> {
                viewModel.setItemEditEnableClick()
            }
            R.id.menu_save -> {
                AlertDialog.Builder(this)
                    .setTitle("Save")
                    .setPositiveButton("OK") { s, s1 ->
                        viewModel.saveFileText(b.editViewFile.text.toString())
                        Toast.makeText(this, "Saved", Toast.LENGTH_SHORT).show()
                    }
                    .setNegativeButton("Cancel") { s, s1 ->

                    }.show()
            }
            R.id.menu_setting -> {
                val dialog = DialogSettingEditView(this).apply {
                    viewModel.onTextSizeChangeComple = {
                        b.tvTextSize.setText("$it sp")
                    }

                    b.tvTextSize.setText("${viewModel.getTextSize().value} sp")
                    b.btnOk.setOnClickListener {
                        dialog.cancel()
                        viewModel.saveTextSize()
                    }

                    b.imgGiam.setOnClickListener {
                        viewModel.setTextSizeReduce()
                    }
                    b.imgTang.setOnClickListener {
                        viewModel.setTextSizeIncrease()
                    }
                }.show()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        if (viewModel.fileText.content != b.editViewFile.text.toString()) {
            AlertDialog.Builder(this)
                .setTitle("Lưu")
                .setMessage("Bạn đã chỉnh sửa nhưng chưa lưu. Lưu và thoát?")
                .setPositiveButton("Lưu") { s, s1 ->
                    viewModel.saveFileText(b.editViewFile.text.toString())
                    Toast.makeText(this, "Đã lưu", Toast.LENGTH_SHORT).show()
                    super.onBackPressed()
                }.setNegativeButton("Hủy") { s, s1 ->
                    super.onBackPressed()
                }.show()
        } else {
            super.onBackPressed()
        }
    }


    override fun onStart() {
        super.onStart()
    }

    override fun onResume() {
        super.onResume()
        Log.d("AAA", "onresum")
    }


    override fun onPause() {
        super.onPause()
        Log.d("AAA", "onPause")
    }

    override fun onStop() {
        super.onStop()
        Log.d("AAA", "onStop")
    }

    override fun onRestart() {
        super.onRestart()
        Log.d("AAA", "onReset")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("AAA", "onDestroy")
    }


}