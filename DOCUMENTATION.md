# Python geek

### Ukratko

**Python geek** je android aplikacija pomoću koje studenti na Prirodno-matematičkom fakultetu Univerziteta u Sarajevu koji slušaju predmet Programiranje 1 mogu provjeriti svoje znanje iz osnova programiranja i programskog jezika Python. Aplikacija je napravljena kao projekat u sklopu predmeta "Razvoj mobilnih aplikacija". Aplikacija je pisana u programsku jeziku Kotlin.
Naziv aplikacije je proistekao iz činjenice da je kviz o programskom jeziku Python, a logo aplikacije je kreiran na web stranici [ucraft.com](https://www.ucraft.com/free-logo-maker).

- Predmet: Razvoj mobilnih aplikacija
- Profesor : Elmedin Selmanović
- Studentice : Adisa Bolić i Amina Sinanović

![pythonGeek](https://i.ibb.co/QMTqzWd/logo-about.png) ![pythonGeek_bs](https://i.ibb.co/p29qn55/logo-about-bs.png)

### O kvizu

Kviz sadržava pitanja različitog tipa i težine. Tipovi su: pitanja sa višestrukim odgovorom (jedan tačan ili više tačnih) i unos odgovora (nekoliko riječi). Pitanja su kategorisana u tri nivoa težine. 
Prije početka kviza korisnik bira težinu kviza (jednu od tri težine) i broj pitanja. Prilikom odgovaranja na pitanje korisnik ima mogućnost na “džoker” pitaj prijatelja te po okončanju kviza korisniku se prikazuje broj tačnih odgovora sa adekvatnom porukom. Korisnik ima mogućnost dijeljenja rezultata sa svojim prijateljima.

### Arhitektura aplikacije

Aplikaciju čini jedna glavna aktivnost *MainActivity* i više fragmenata koji su podijeljeni u 4 paketa ovisno od toga za koji dio su vezani, tako imamo sljedeće pakete: game, score, menu i title paket.
Pri izradi aplikacije pokušano je zadovoljiti tzv. *seperation of concerns* princip.

Separation of concerns
:	odnosno "podjela zaduženja" je princip po kojem se smislene cjeline koda odvajaju u zasebne odjeljke kako bi bilo olakšano mijenjanje pojedinačnih dijelova koda, pronalazak grešaka i proširivanje aplikacije.

Tako u paketu *game* imamo 3 dokumenta: *GameFragment* - u kojem se kreira fragment i definiše prikaz pitanja, *GameViewModel* - u kojem je definisana sva logika za kviz i *GameViewModelFactory* - u kojem je definisano na koji način se kreira GameViewModel. Slično su podijeljeni i dokumenti u score paketu.

![fragmentPackage](https://i.ibb.co/WvgxKpX/fragment-Packages.png)

Svi podaci vezani za kviz nalaze se u bazi. Korištena je Google-ova Firebase.

Firebase
:	je NoSQL baza podataka koja omogućava spremanje, dohvaćanje i sinhronizovanje podataka u njoj u realnom vremenu. Prednost ove baze podataka je i njena kompaktibilnost s Android Studiom.

Ostali resursi koji se koriste u aplikaciji kao što su dimenzije, boje, stringovi i naravno layouti nalaze se u res folderu projekta. Svi tekstovi su dostupni na bosanskom i na engleskom jeziku, te su podržani različiti layouti za horizontalnu i vertikalnu orijentaciju mobilnog uređaja.

![resourceHierarcy](https://i.ibb.co/890GB9J/layout-Hierarcy.png)

#### Paket: title

U ovom paketu nalazi se samo jedan dokument **TitleFragment** za kojeg je vezan *title_fragment.xml* layout. Pored kreiranja fragmenta i prikaza početne stranice aplikacije, u ovom dokumentu su definisane radnje za navigaciju kroz aplikaciju i kreiranje meni opcija koje nudi aplikacija. Također, ovdje dohvaćamo podatke koje korisnik unosi, a koji se odnose na težinu kviza i broj pitanja.

Izgled početne stranice aplikacije u vertikalnom i horizontalnom položaju uređaja:

![title_land](https://i.ibb.co/7NqLpBX/title-fragment-land.png) ![title](https://i.ibb.co/gF3x32v/title-fragment.png)

#### Paket: menu

Aplikacija posjeduje *drawable menu* to jest meni koji se prikazuje kada pređemo desno preko aplikacije. U meniju se nalaze tri opcije, te samim tim u ovom paketu imamo tri fragmenta, po jedan za svaku opciju:
- AboutFragment
- RulesFragment
- UsefulLinks

*AboutFragment* kreira fragment i definiše sadržaj prozora u kojem se korisnik može upoznati o kakvoj je aplikaciji riječ, koja verzija, te o dizajnerkama aplikacije i direktno otići na njihove facebook profile ili ih kontaktirati preko mail pošte, a što je ostvareno eksternim linkovima.

*RulesFragment* kreira fragment i definiše sadržaj prozora u kojem se nalaze pravila za kviz.

*UsefulLinks* kreira fragment i definiše sadržaj prozora u kojem se nalaze linkovi na razne knjige i web stranice odakle korisnik može učiti Python.

Za ljepši izgled About stranice korišten je template preuzet sa github-a, a koji se može pronaći [ovdje](https://github.com/medyo/android-about-page).

![menu_bs](https://i.ibb.co/DR7KQ4V/menu-bs.png) ![menu](https://i.ibb.co/ng2Rq8Q/menu.png)

Izgled stranica za pravila, linkove i stranice o aplikaciji:

![rules](https://i.ibb.co/rdPvc0Y/rules.png) ![links](https://i.ibb.co/znHkSKf/links.png) ![about](https://i.ibb.co/7rCGL9b/about.png)

#### Paket: game

Game paket se sastoji iz sljedećih dokumenata:
- GameFragmenta
- GameViewModel
- GameViewModelFactory

Unutar **GameFragment** dokumenta kreira se fragment za kviz. Po kreiranju odmah se vezuje za odgovarajuči ViewModel. ViewModel je klasa koja je dizajnirana da sprema i manipuliše podacima koji se odnose na dio korisničkog interfejsa, a sve to uzimajući u obzir *lifecycle* fragmenta, odnosno životni vijek fragmenta.

Lifecycle
:	Svaka aktivnost i fragment ima svoj životni vijek, od kreiranja do njenog uništenja. U međuvremenu aktivnost, odnosno fragment, može biti u različitim stanjima (start, restart, resume, pause, stop) i potrebno je definisati šta se dešava kada aplikacija prelazi iz jednog u drugo stanje: koje aktivnosti nastavlja obavljati, koje zaustavlja, koje podatka čuva, koje resetuje, kakav će biti prikaz aktivnosti, odnosno fragmenta, kada aplikacija bude ponovo na ekranu i slično.
Praćenje životnog vijeka je veoma bitan dio aplikacije i nimalo jednostavan jer može doći do gubljenja podataka, te jer se promjene stanja dešavaju u raznim situacijama i lahko je neke predvidjeti. Android Studio nudi odličan alat koji olakšava sav taj posao kroz kreiranje ViewModela.

Kviz nudi tri različita tipa pitanja. To je omogućeno na način da je *game_fragment.xml* podijeljen na više dijelova: dio za prikaz težine kviza i redni broj pitanja, dio u kojem se nalazi layout za tekst pitanje (*question.xml*), dio za odgovore u koji se uključuju layouti za sva tri tipa odgovora (*text_answer.xml*,*multiple_choice.xml*, *checkbox_answer.xml*), a koji se pak prikazuju ovisno od tipa trenutnog pitanja, te dio u kojem se nalazi Answer/Odgovori dugme. Ukoliko se u pitanju nalazi dio koda, tada je kod istaknut od ostatka teksta kako bi se korisniku naznačilo da se radi o kodu.

Pitanja s višestrukim odgovorom mogu imati neograničen broj ponuđenih odgovora, a to je postignuto dinamičkim dodavanjem *RadioButton* i *CheckBox* kroz kod u *GameFragment*.

Za vrijeme kviza u gornjem desnom uglu, umjesto opstions menija, nalazi se ikona za iskorištavanje "džokera". Kako je rečeno u opisu kviza, u toku kviza korisnik ima pravo jednom pitati prijatelja na pomoć. Klikom na ikonu korisniku se otvara izborni meni u kojem bira aplikaciju pomoću koje će obaviti poziv. Ukoliko naredni put klikne na ikonu prikazat će mu se poruka "Džoker je već iskorišten".

Izgled pitanja s višestrukim odgovorom u kojem je moguće odabrati samo jedan odgovor i skočni prozor nakon klika na ikonu džoker:

![RadioButtonQuestion](https://i.ibb.co/S0jRtdh/radio-Button-Q.png) ![joker](https://i.ibb.co/sRzw70Z/joker.png)

Sva logika vezana za kviz nalazi se u **GameViewModel**. Većina atributa ove klase je tipa *LiveData*.

LiveData
:	je klasa za čuvanje podataka koja registruje promjenu vrijednosti pazeći pri tome na životni vijek fragmenta.

Nakon konektovanja na bazu, uzimaju se sva pitanja i odgovori i smještaju u *LiveData* varijable. Koliko i koja pitanja se uzimaju iz baze definisano je korisnikovim izborom težine kviza i broja pitanja.
Distribucija pitanja definisana je u funkciji chooseQuestions() gdje su nivoi definisani na sljedeći način:
- lagano/easy: 50% lahkih pitanja, 30% srednje teških pitanja i 20% teških pitanja
- srednje teško/medium: 30% lahkih pitanja, 50% srednje teških pitanja i 20% teških pitanja
- teško/hard: 20% lahkih pitanja, 30% srednje teških pitanja i 50% teških pitanja

Pri svakom pozivu funkcije *nextQuestion* uzima se tekst novog pitanja, ponuđeni odgovori (ako je pitanje tog tipa), tačan/tačni odgovori i tip pitanja. Te promjene se automatski detektuju u odgovarajućim prozorima. 
U ovom dokumentu se također nalaze i tri funkcije za obradu odgovora ovisno od tipa pitanja:
- processTextAnswer(ans: String): prima unesenu riječ (ili riječi) od korisnika i provjera da li ta riječ postoji u tačnim odgovorima. Pri provjeri zanemareni su razmaci i ne pravi se razlika između velikih i malih slova.
- processRadioButtonAnswer(ans: Int): prima redni broj ponuđenog odgovora koje je korisnik označio za tačno i prvojerava da li ono uistinu jest tačan odgovor.
- processCheckBoxAnswer(): u ovoj funkciji prolazimo kroz niz pozicija ponuđenih odgovora i provjeravamo da li je korisnik označio samo one i sve one odgovore koji su uistinu tačni.

Ukoliko je odgovor tačan korisnik dobija jedan bod, a u svakom slučaju prelazi na sljedeće pitanje. Navedene funckije se pozivaju nakon što korisnik klikne na Answer/Odgovori dugme pod uslovom da je unio odgovor (napisao riječ ili označio odgovore).

Nakon što korisnik odgovori na sva pitanja poziva se funkcija finishGame() kojom se označava kraj igre i konačan rezultat se prosljeđuje sljedećoj aktivnosti.

Kako bi mogao upravljati podacima pri izmjeni stanja životnog vijeka, veoma je bitno da GameViewModel kreiramo samo jednom, a ne pri svakom kreiranju samog fragmenta. Zato nam služi ViewModelProvider koji vraća već postojeći ViewModel ako postoji, a ako ne postoji kreira novi. Obzirom da pri kreiranju ViewModela postoje parametri koji se prosljeđuju neophodno je definisati na koji način se kreira ViewModel. Za to nam služi Factory design pattern.

Factory design pattern
:	je *creational pattern* koji koristi *factory* metode pri kreiranju objekata, odnosno one metode koje vraćaju instance iste te klase.

U dokumentu **GameViewModelFactory** definišemo na koji način će se kreirati GameViewModel i koji parametri će biti proslijeđeni, konkretno to su težina kviza koju je korisnik odabrao i broj pitanja.

#### Paket: Score

U paketu score nalaze se dokumenti za kreiranje fragmenta koji omogućava prikaz odgovarajućeg layouta po okončanju igre, te nudi opcije dijeljenja konačnog rezultata sa prijateljima i ponovno započinjanje kviza.
Paket se sastoji od:
- ScoreFragment
- ScoreViewModel
- ScoreViewModelFactory

**ScoreFragment** kreira fragment i za njeg veže odgovarajuči ViewModel u kojem se čuva konačan rezultat kviza, a koji se dobija iz prethodnog fragmenta *GameFragment* uz pomoć Gradle dodatka SafeArg.

SafeArgs
:	je Gradle dodatak koji generiše kod i klase koje pomažu da se pogrešaka pri prijenosu podataka između aktivnosti otkriju za vrijeme kompajliranja, a koje se inače ne mogu detektovati dok se aplikacija ne pokrene.

U ovom fragmentu je omogućeno korisniku da podijeli svoj rezultat. Za to se koristi svojstvo Androida koje dopušta aplikaciji da implicitnim *intentom* korisnika uputi na druge aplikacije na njegovom mobilnom uređaju ovisno od akcije koju želi izvršiti. 

Intent
:	je objekt koji sadrži jednostavne poruke za komuniciranje Androidovih komponenti. S implicitnim *intentom* pokreće se aktivnost bez definisanja i znanja o tome koja će aplikacija ili aktivnost izvršiti zadatak. Ukoliko više Android aplikacija na uređaju može izvršiti isti implicitni intent tada se korisniku otvara izborni meni u kojem on bira koja će aplikacija izvršiti zadatak.

Kada korisnik kline na ikonu za dijeljenje rezultate, u izborniku će dobiti sve aplikacije koje mogu procesirati tekstualnu poruku i  nalaze se na njegovom uređaju, poput aplikacija za slanje poruka ili objavljivanje tekstualnih postova.

**ScoreViewModel** služi za čuvanje podataka o rezultatu i preuzima na sebe brigu o čuvanju istog pri promjeni stanja u životnom ciklusu fragmenta.

**ScoreViewModelFactory** definiše na koji način se kreira ViewModel i koji se parametri prosljeđuju. Konkretno, riječ je o samo jednom parametru i to rezultatu kviza koji je proslijeđen iz *GameFragmenta*.

Izgled: zadnje stranice i prikaz rezultate, te prikaz na koji način se može dijeliti rezultat:

![score](https://i.ibb.co/Zdtrjjm/score.png)  ![shareScore](https://i.ibb.co/868bszB/share-Score.png)

#### Baza

Kao što je navedeno, za bazu je korišten Firebase. Obzirom da nije riječ o lokalnoj bazi i ne koristi se SQLLite server, konektovanje na bazu i uzimanje svih potrebnih podataka vrlo lahko se vrši u GameViewModel. U bazi se nalazi jedna kolekcija *pitanje* čija su polja:
- *tekst*: tekst pitanja
- *tajmer*: broj sekundi za koje korisnik mora odgovoriti na pitanje, ako je *null* korisnik nije ograničen vremenom 
- *tip*: tip pitanja (1, 2 ili 3), na osnovu kojeg znamo koji layout uključujemo za odgovajarujići unos odgovora
- *tezina*: tezina (0, 1 ili 2) 
- *ponudjen*: lista ponuđenih odgovora, može biti *null* ako je tip pitanja 1 tj. ako je riječ o pitanju s tekstualnim odgovorom
- *tacanPonudjen*: lista boolean vrijednosti koja na i-toj poziciji ima vrijednost tačno, odnosno netačno, ovisno od toga da li je *ponudjen* odgovor na i-toj poziciji tačan, odnosno netačan; *null* ako je pitanje tipa 1
- *tacanTekst*: lista odgovora koji se mogu uzeti za tačne na odgovarajuće pitanje s tekstualnim odgovorom, ako je pitanje tipa 2 ili 3 onda je vrijednost *null* 

Obzirom da se sva pitanja za kviz unose prije pokretanja istog, dovoljan je samo jedan pristup bazi podataka i to pri kreiranje viewModela za kviz. Odjednom se uzimaju svi podaci u liste, a zatim se iz listi izvlače potrebni podaci.

#### Dizajn

Prilikom kreiranja izgleda aplikacije težile smo **jednostavnosti** i **minimalizmu**. To se vidi iz elemenata koji su zastupljeni u dizajnu, a riječ je isključivo o jasno definisanim, jednostavnim geometrijskim likovima. Dalje, izbjegavane su jarke boje (osnovne boje u aplikaciji su nijanse zelene, crvene i sive) i korišten je negativan prostor. 
Jednostavnost se vidi i u tzv. *monospaced* fontu. Font je preuzet sa github-a, može se pronaći [ovdje](https://github.com/tonsky/FiraCode). Sav dizajn je prilagodljiv različitim uređajima te su izbjegavane konkretne veličine i korišteni parametri poput WRAP_CONTENT i MATCH_PARENT što je više moguće.

#### Navigacija

Navigiranje kroz različite fragmente urađeno je uz pomoć Androidovih komponenti za navigiranje: NavGraph, NavHost i NavController. Pored akcija za prelazak s jednog fragmenta na drugi, te prenos argumenata, u ovom dijelu definisano je kako se ponaša sistemski Back button te je dodan i Up button u AppBar. U AppBaru se nalazi i options menu.

Korisnik s početne strane može otići na fragment kviza, a po završetku kviza odlazi na fragment u kojem mu se ispisuje rezultat. Klikom na dugme Play again/Igraj ponovo može otići ponovo na početnu stranu i izabrati novu težinu kviza i broj pitanja. Vraćanje nazad u bilo kojem trenutku omogućeno je uz Back i Up dugmadi, no vraćanje na pitanje je zabranjeno.

Prikaz *navigation.xml* u kojem se jasno vidi za koje fragmente je definisana akcija i navigiranje između njih:
![navigation](https://i.ibb.co/tz4j8Zm/navigation.png)

#### Ostale specifikacije

Aplikacija je podržana na dva jezika: bosanski i engleski jezik. Ovo svojstvo je dodano pomoću Androidovog alata Translation Editor. U res folderu postoje dva dokumenta koji čuvaju resurse stringova, jedan na engleskom i jedan na bosanskom. Jezik se prilagođava jeziku mobilnog uređaja, odnosno ukoliko korisnik želi koristiti aplikaciju na bosanskom jeziku potrebno je u postavkama odabrati da jezik uređaja bude bosanski.

Ni u jednom trenutku ne dolazi do gubljenja podataka usljed promjene stanja za vrijeme životnog ciklusa fragmenata i aktivnosti uključujući i minimiziranje aplikacije, prebacivanje fokusa na izborne menije te rotaciju ekrana.

Kreirani su različiti layouti za vertikalnu i horizontalnu orijentaciju uređaja radi ljepšeg i prirodnijeg prikaza elemenata.

Pažnja je posvećena provjerama ispravnog unosa i onemogućavanju neželjenih situacija. S tim u vezi korisnik ne može preći na naredno pitanje prije nego unese odgovor, ne može se vratiti na prethodno pitanje, broj pitanja koje korisnik želi je uvijek manji od broja pitanja dostupnih u bazi i brojna druga ograničenja.

Izbjegavano je hardkodiranje resursa kako tekstova, tako i boja, dimenzija i stilova teksta.

#### O procesu pravljenja aplikacije

Projekat je na samom startu postavljen na gitlab-u i može se pronaći [ovdje](https://gitlab.com/adisa.bolic/programiranje-kviz). Zahvaljujući ugrađenim opcijama u Android Studio povezivanje sa gitlabom i vršenje osnovnih operacija (commit, pull, push, merge) je bilo jednostavno i lahko.

Android Studio se ponovo pokazao kao odličan framework. Zadivljujući su njegovi prijedlozi programeru koji dosta olakšavaju rad, do koje mjere provjera sintaksu i nudi njeno pojednostavljenje.
Najveći izazov pri izradi projekta bilo je kreiranje i uklapanje raznih layouta: vođenje računa da je dizajn prilagodljiv raznim uređajima, da ne dolazi do sukobljavanja i preklapanja elemenata u layoutu i dodavanja određenih Views iz fragmenta kroz kod.
Nažalost, radiobutton i checkbox nisu prikazani upotrebom RecyclerView. I dok je to dosta elegantiji pristup u ovom konkretnom slučaju je i daleko komplikovaniji. Obzirom da recycleView koristi već postojeće elemente za prikaz novih podataka, te da se radiobuttoni ne nalaze unutar jedne grupe, praćenje koje dugme je označeno i pri kojem ponuđenom odgovoru te dopuštanje samo jedne oznake isuviše je komplikovano na ovaj način.

Aplikacija je pisana u programskom jeziku Kotlin. Aplikacija je testirana na Nexus 5 emulatoru, a omogućena je na mobilnim uređajima s Android verzijom 5.1 pa do najnovije.

Pokretanje koda na emulatoru:

![androidStudioEmulator](https://i.ibb.co/BzGLjqW/emulator.png)

#### Mogućnost nadogradnje

Kroz izradu aplikacije nastojale smo poštivati dobre prakse u Androidu i pridržavati se osnovnih principa izrade aplikacije. Skalabilnost, modularnost i konektovanje na bazu ostavlja prostora za dodavanje raznih dodatnih opcija.
Uključivanjem autentifikacije, jednostavno bi bilo omogućiti kreiranje accounta i spašavanje rezultata korisnikovih testova u bazu.
Uz nepretjerane izmjene može se postići da pored težine i broja pitanja, korisnik bira i predmet iz kojeg želi provjeriti znanje.
Trenutna aplikacija podržava i dodavanje novih jezika, tipova pitanja, negativnih bodova, animacija, te razne druge dodatke.
