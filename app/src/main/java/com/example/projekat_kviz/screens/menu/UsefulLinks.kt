/* The MIT License (MIT)
Copyright (c) 2016 Mehdi Sakout

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
*/
package com.example.projekat_kviz.screens.menu

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.projekat_kviz.R
import mehdi.sakout.aboutpage.AboutPage
import mehdi.sakout.aboutpage.Element

class UsefulLinks : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val copyrightElement = Element()
        copyrightElement.title = getString(R.string.copyright)
        val link1 = Element()
        link1.title = getString(R.string.link1_text)
        link1.iconDrawable = R.drawable.link
        link1.setOnClickListener{
            val url = getString(R.string.link1)
            val i = Intent(Intent.ACTION_VIEW)
            i.data = Uri.parse(url)
            startActivity(i)
        }
        val link2 = Element()
        link2.title = getString(R.string.link2_text)
        link2.iconDrawable = R.drawable.link
        link2.setOnClickListener{
            val url = getString(R.string.link2)
            val i = Intent(Intent.ACTION_VIEW)
            i.data = Uri.parse(url)
            startActivity(i)
        }
        val link3 = Element()
        link3.title = getString(R.string.link3_text)
        link3.iconDrawable = R.drawable.link
        link3.setOnClickListener{
            val url = getString(R.string.link3)
            val i = Intent(Intent.ACTION_VIEW)
            i.data = Uri.parse(url)
            startActivity(i)
        }
        val link4 = Element()
        link4.title = getString(R.string.link4_text)
        link4.iconDrawable = R.drawable.link
        link4.setOnClickListener{
            val url = getString(R.string.link4)
            val i = Intent(Intent.ACTION_VIEW)
            i.data = Uri.parse(url)
            startActivity(i)
        }

        val page = AboutPage(context)
            .isRTL(false)
            .setDescription(getString(R.string.links_text))
            .setImage(R.drawable.logo)
            .addItem(link1)
            .addItem(link2)
            .addItem(link3)
            .addItem(link4)
            .addItem(copyrightElement)
            .create()
        return page
    }

}
