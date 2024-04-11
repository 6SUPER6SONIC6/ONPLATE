package com.supersonic.onplate.utils

import android.content.ContentResolver
import android.net.Uri
import java.io.IOException

fun validateUri(uri: Uri, contentResolver: ContentResolver) : Boolean {
    val uriValid = try {
        contentResolver.openInputStream(uri)?.use {  }
        true
    } catch (e: IOException){
        false
    }

    return uriValid

}

fun validateUriList(list: List<Uri>, contentResolver: ContentResolver) : List<Uri> {
    val validatedList = mutableListOf<Uri>()
    list.forEach { uri ->
        if (validateUri(uri, contentResolver)){
            validatedList.add(uri)
        }
    }

    return validatedList
}