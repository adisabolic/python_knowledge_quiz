/* The MIT License (MIT)
Copyright (c) 2016 Mehdi Sakout

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
*/
package com.example.projekat_kviz.screens.menu

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.projekat_kviz.R
import mehdi.sakout.aboutpage.AboutPage
import mehdi.sakout.aboutpage.Element


class AboutFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val versionElement = Element()
        versionElement.title = "Version 1.0"
        val copyrightElement = Element()
        copyrightElement.title = getString(R.string.copyright)

        val page = AboutPage(context)
            .isRTL(false)
            .setDescription(getString(R.string.about_text))
            .setImage(R.drawable.logo)
            .addItem(versionElement)
            .addGroup("Adisa Bolic")
            .addEmail("adisa.bolic@gmail.com", "Email")
            .addFacebook("adisa.bolic")
            .addGroup("Amina Sinanovic")
            .addEmail("sinanovica1@gmail.com", "Email")
            .addFacebook("sinanananaa")
            .addItem(copyrightElement)
            .create()
        return page
    }
}
