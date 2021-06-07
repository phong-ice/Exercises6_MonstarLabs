package com.example.myapplication.activity

import android.Manifest
import android.R.attr
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.FileUtils
import android.provider.DocumentsContract
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.lifecycle.ViewModelProvider
import com.example.myapplication.databinding.ActivitySettingBinding
import com.example.myapplication.databinding.LayoutDialogBackupBinding
import com.example.myapplication.helper.RealPathUtil
import com.example.myapplication.helper.Ultils.KEY_SPACE
import com.example.myapplication.helper.Ultils.NEW_LINE
import com.example.myapplication.model.Diary
import com.example.myapplication.repository.DiaryRepository
import com.example.myapplication.viewmodel.DiaryViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.*
import java.net.URI
import java.net.URL


class Setting : AppCompatActivity() {

    private val binding: ActivitySettingBinding by lazy {
        ActivitySettingBinding.inflate(
            layoutInflater
        )
    }
    private val repo: DiaryRepository by lazy { DiaryRepository(application) }
    private val diaryViewModel: DiaryViewModel by lazy {
        ViewModelProvider(
            this,
            DiaryViewModel.DiaryViewModelProvide(repo)
        )[DiaryViewModel::class.java]
    }
    private val STORAGE_REQUEST_CODE_EXPORT: Int = 1
    private val STORAGE_REQUEST_CODE_IMPORT: Int = 2
    private lateinit var storagePermission: Array<String>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        supportActionBar?.title = "Setting"
        storagePermission = arrayOf(
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE
        )

        binding.btnPassword.setOnClickListener {
            startActivity(Intent(this, SettingPassword::class.java))
        }

        binding.btnBackup.setOnClickListener {
            if (checkPermission()) setNamFileBackup()
            else requestStoragePermissionExport()
        }
        binding.btnRestore.setOnClickListener {
            if (checkPermissionRead()) {
                openFile()
            } else {
                requestStoragePermissionImport()
            }
        }
    }

    private fun checkPermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            this,
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE
        ) == (PackageManager.PERMISSION_GRANTED)
    }

    private fun checkPermissionRead(): Boolean {
        return ContextCompat.checkSelfPermission(
            this,
            android.Manifest.permission.READ_EXTERNAL_STORAGE
        ) == (PackageManager.PERMISSION_GRANTED)
    }


    private fun requestStoragePermissionExport() {
        ActivityCompat.requestPermissions(this, storagePermission, STORAGE_REQUEST_CODE_EXPORT)
    }

    private fun requestStoragePermissionImport() {
        ActivityCompat.requestPermissions(this, storagePermission, STORAGE_REQUEST_CODE_IMPORT)
    }


    private fun exportFile(name: String) {
        val folder = File(getExternalFilesDir(null), "diary_app")
        if (!folder.exists()) folder.mkdir()
        val nameFile = "$name.csv"
        val fileNameAndPath = "$folder/$nameFile"
        val fw = FileWriter(fileNameAndPath)
        diaryViewModel.getAllDiary().observe(this, {
            try {
                for (diary in it) {
                    fw.append("${diary.idDiary}")
                    fw.append(KEY_SPACE)
                    fw.append(diary.titleDiary.replace("\n".toRegex(),NEW_LINE))
                    fw.append(KEY_SPACE)
                    fw.append(diary.contentDiary.replace("\n".toRegex(),NEW_LINE))
                    fw.append(KEY_SPACE)
                    fw.append(diary.dateDiary)
                    fw.append("\n")
                }
                fw.flush()
                Toast.makeText(this, "Backup data successful", Toast.LENGTH_SHORT).show()
            } catch (e: Exception) {
                Toast.makeText(this, e.message, Toast.LENGTH_SHORT).show()
            } finally {
                fw.close()
                diaryViewModel.deleteAllDiary()
            }
        })
    }

    private fun openFile() {
        var intent = Intent(Intent(Intent.ACTION_GET_CONTENT))
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.type = "*/*"
        intent.putExtra(DocumentsContract.EXTRA_INITIAL_URI, "pickerInitialUri")
        startActivityForResult(Intent.createChooser(intent, "Open CSV"), 756)
    }

    private fun setNamFileBackup() {
        val builder = AlertDialog.Builder(this)
        val _binding: LayoutDialogBackupBinding = LayoutDialogBackupBinding.inflate(layoutInflater)
        builder.setView(_binding.root)
        builder.setTitle("Backup")
        builder.setMessage("If you want backup, all your record in app will removed ")
        builder.setPositiveButton("Create") { dialog, _ ->
            when (val nameFile = _binding.edtNameFile.text.toString()) {
                "" -> {
                    Toast.makeText(this, "Require enter your name file", Toast.LENGTH_SHORT).show()
                }
                else -> exportFile(nameFile)
            }
        }
        builder.setNegativeButton("Cancel") { dialog, _ ->
            dialog.dismiss()
        }
        val alertDiaLog = builder.create()
        alertDiaLog.show()
    }

    private fun importFile(path: Uri) {
        try {
            val fileOutputStream = contentResolver.openInputStream(path)
            val reader = InputStreamReader(fileOutputStream)
            val bufferReader = BufferedReader(reader)
            var line = bufferReader.readLine()
            while (line != null) {
                val arr: List<String> = line.split(KEY_SPACE)
                if (arr.size < 5) {
                    val titleDiary = arr[1].replace(NEW_LINE,"\n")
                    val contentDiary = arr[2].replace(NEW_LINE,"\n")
                    val dateDiary = arr[3]
                    CoroutineScope(Dispatchers.IO).launch {
                        val id = diaryViewModel.insertDiary(
                            Diary(
                                0,
                                titleDiary,
                                contentDiary,
                                dateDiary
                            )
                        )
                    }
                    line = bufferReader.readLine()
                }
            }
            fileOutputStream?.close()
            reader.close()
            Toast.makeText(this, "Restore successful....", Toast.LENGTH_SHORT).show()
        } catch (e: java.lang.Exception) {
            Log.i("test123", "Exception :${e.message.toString()}")
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            STORAGE_REQUEST_CODE_IMPORT -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    openFile()
                } else {
                    Toast.makeText(this, "Permission denied....", Toast.LENGTH_SHORT).show()
                }
            }
            STORAGE_REQUEST_CODE_EXPORT -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    setNamFileBackup()
                    Toast.makeText(this, "Backup successful....", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Permission denied....", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            756 -> {
                if (resultCode == RESULT_OK) {
                    data?.data.also {
                        importFile(it!!)
                    }
                }
            }
        }
    }

}