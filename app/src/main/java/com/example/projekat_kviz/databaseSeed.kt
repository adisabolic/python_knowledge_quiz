package com.example.projekat_kviz

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore

data class Pitanje(
    val tekst: String? = null,
    val tip: Int? = null, // 1 za tekst, 2 za visetruki 1 odabir, 3 za visestruki vise odabira
    val tajmer: Int? = null,
    val tezina: Int? = null, // 0  1 ili 2
    val tacanTekst: List<String>? = null,
    val ponudjen: List<String>? = null,
    val tacanPonudjen: List<Boolean>? = null
)

fun importData() {
    val db = FirebaseFirestore.getInstance()
    val docRef = db.collection("pitanje")

    var q = Pitanje("Is Python case sensitive when dealing with identifiers?", 2, 30, 1,
        listOf(), listOf("yes", "no", "machine dependent", "none of the mentioned"), listOf(true, false, false, false))
    docRef.add(q)

    q = Pitanje("Which of the following is invalid?", 2, 30, 0,
        listOf(), listOf(" _a = 1", "__a = 1", "__str__ = 1", "none of the mentioned"), listOf(false, false, false, true))
    docRef.add(q)

    q = Pitanje("What is the output of the following code? \n " +
            "/code/for i in range(10, 15, 1):\n" +
            "    print( i, end=', ')/-code/", 1, 40, 0,
        listOf("10, 11, 12, 13, 14"), listOf(), listOf())
    docRef.add(q)

    q = Pitanje("Which of these operators are logical operators?", 3, 30, 0,
        listOf(), listOf("and", "/", "&", "not"), listOf(true, false, false, true))
    docRef.add(q)

    q = Pitanje("Select all correct ways to copy a dictionary in Python", 3, 45, 2,
        listOf(), listOf("dict2 = dict1.copy()", "dict2 = dict(dict1.items())/-code/",
            "dict2 = dict(dict1)", "dict2 = dict1"),
        listOf(true, false, true, true))
    docRef.add(q)

    q = Pitanje("Python function always returns a value", 2, 25, 0,
        listOf(), listOf("True", "False"), listOf(false, true))
    docRef.add(q)

    q = Pitanje("What is the output of the following function call \n " +
            "/code/def fun1(name, age=20):\n" +
            "    print(name, age)\n" +
            "\n" +
            "fun1('emma', 25)/-code/", 1, 60, 1,
        listOf("emma 25"), listOf(), listOf())
    docRef.add(q)

    q = Pitanje("What is the output of the following function call\n" +
            "/code/def fun1(num):\n" +
            "    return num + 25\n" +
            "\n" +
            "fun1(5)\n" +
            "print(num)/-code/", 1, 60, 1,
        listOf("nameerror", "name error", "name eror", "nameeror"), listOf(), listOf())
    docRef.add(q)

    q = Pitanje("What is the output of the add() function call\n" +
            "/code/def add(a, b):\n" +
            "    return a+5, b+5\n" +
            "\n" +
            "result = add(3, 2)\n" +
            "print(result)/-code/", 2, 60, 1,
        listOf(), listOf("15", "8", "(8, 7)", "Syntax Error"), listOf(false, false, true, false))
    docRef.add(q)

    q = Pitanje("Select true statements for Python functions", 3, 0, 1,
        listOf(), listOf("A function is a code block that only executes when it is called.",
            "Python function always returns a value.",
            "A function only executes when it is called and we can reuse it in a program",
            "Python doesn’t support nested function"), listOf(true, false, true, false))
    docRef.add(q)

    q = Pitanje("/code/if -3/-code/ will evaluate to true", 2, 45, 0,
        listOf(), listOf("True", "False"), listOf(true, false))
    docRef.add(q)

    q = Pitanje("What is the output of the following range() function\n" +
            "/code/for num in range(2,-5,-1):\n" +
            "    print(num, end=\", \")/-code/", 2, 40, 1,
        listOf(), listOf("2, 1, 0", "2, 1, 0, -1, -2, -3, -4", "2, 1, 0, -1, -2, -3, -4, -5"), listOf(false, true, false))
    docRef.add(q)

    q = Pitanje("What is the output of the following nested loop\n" +
            "/code/for num in range(10, 14):\n" +
            "   for i in range(2, num):\n" +
            "       if num%i == 1:\n" +
            "          print(num)\n" +
            "          break/-code/", 2, 90, 2,
        listOf(), listOf("10\n11\n12\n13", "11\n13"), listOf(true, false))
    docRef.add(q)

    q = Pitanje("What is the value of the var after the for loop completes its execution\n" +
            "/code/var = 10\n" +
            "for i in range(10):\n" +
            "    for j in range(2, 10, 1):\n" +
            "        if var % 2 == 0:\n" +
            "            continue\n" +
            "            var += 1\n" +
            "    var+=1\n" +
            "else:\n" +
            "    var+=1\n" +
            "print(var)/-code/", 2, 0, 2,
        listOf(), listOf("20", "21", "10", "30"), listOf(false, true, false, false))
    docRef.add(q)

    q = Pitanje("What is the output of the following code\n" +
            "/code/str1 = \"pynative\"\n" +
            "print(str1[1:4], str1[:5], str1[4:], str1[0:-1], str1[:-1])/-code/", 1, 0, 2,
        listOf("yna pynat tive pynativ pynativ"), listOf(), listOf())
    docRef.add(q)

    q = Pitanje("Select all the correct options to remove “Orange” from the set.\n" +
            "/code/sampleSet = {\"Yellow\", \"Ornage\", \"Black\"}/-code/", 3, 90, 2,
        listOf(), listOf("sampleSet.pop(“Orange”)", "ampleSet.discard(“Orange”)",
            "sampleSet.remove(“Orange”)", "del sampleSet [“Orange”]"), listOf(true, false, true, true))
    docRef.add(q)

    q = Pitanje("Select all the correct options to join two lists in Python\n" +
            "/code/listOne  =  ['a', 'b', 'c', 'd']\n" +
            "listTwo =  ['e', 'f', 'g']/-code/", 3, 0, 2,
        listOf(), listOf("newList = listOne + listTwo", "newList = extend(listOne, listTwo)",
            "newList = listOne.extend(listTwo)", "newList.extend(listOne, listTwo)"), listOf(true, false, true, false))
    docRef.add(q)
}