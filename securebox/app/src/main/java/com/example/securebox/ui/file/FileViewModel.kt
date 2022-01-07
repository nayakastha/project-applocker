package com.example.securebox.ui.file

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.securebox.util.EncryptionHelper
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.*

class FileViewModel(application: Application) : AndroidViewModel(application) {

    val snackBarMsg: MutableLiveData<String> = MutableLiveData()
    private val dateFormat = SimpleDateFormat("HH:mm:ss", Locale.getDefault())
    private val context = application
    private val dir = File(context.filesDir, "documents")
    val content: MutableLiveData<String> = MutableLiveData()

    fun storeFile() {
        viewModelScope.launch {
            val date = Date()
            val fileName = "${dateFormat.format(date)}.txt"
            if (!dir.exists()) {
                dir.mkdir()
            }
            val file = File(dir, fileName)

            launch(IO) {
                try {
                    val fileOutputStream = FileOutputStream(file)
                    fileOutputStream.write(content.value?.toByteArray())
                    fileOutputStream.flush()
                    fileOutputStream.close()
                    snackBarMsg.postValue("File saved successfully!")

                } catch (e: Exception) {
                    snackBarMsg.postValue(e.message)
                }
            }
        }
    }

    fun storeEncryptedFile() {
        viewModelScope.launch {
            val date = Date()
            val fileName = "Encrypted ${dateFormat.format(date)}.txt"
            if (!dir.exists()) {
                dir.mkdir()
            }
            val file = File(dir, fileName)

            launch(IO) {
                try {
                    val encryptedFile = EncryptionHelper.getEncryptedFile(file, context)
                    encryptedFile.openFileOutput().also {
                        it.write(content.value?.toByteArray())
                        it.flush()
                        it.close()
                    }

                    snackBarMsg.postValue("File saved successfully!")

                } catch (e: Exception) {
                    snackBarMsg.postValue(e.message)
                }
            }
        }
    }

}