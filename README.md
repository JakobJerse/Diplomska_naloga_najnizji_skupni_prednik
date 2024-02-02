# :closed_book: Diplomska naloga - Iskanje najnižjega skupnega prednika vozlišč v drevesu 
Repozitorij vsebuje izvorno kodo implementacij petih različnih algoritmov, ki rešujejo problem najnižjega skupnega prednika v drevesnih strukturah, ter metode za testiranje pravilnosti implementacij.


## :computer: Tehnične podrobnosti:
Celotna izvorna koda je napisana v programskem jeziku **Java** :coffee:, uporabljena pa je bila verzija *java 15*. Rezultati so grafično
prikazani z orodjem JFreeChart. 

:exclamation: V kolikor se bo bralec lotil evalvacije na svojem računalniku, je potrebno omeniti, da se rezultati lahko razlikujejo od teh, ki so predstavljeni 4. poglavju diplomske naloge, saj so le-ti med drugim odvisni tudi od sistema, na katerem se evalvacija izvaja. Rezultati ki se nahajajo v tem repozitoriju, so bili pridobljeni tekom evalvacije, ki se je izvajala na prenosnem računalniku z 2.6 GHz 6-jedrnim Intel i7 procesorjem, 16 GB RAM-a in grafično kartico AMD Radeon Pro 5300M z 4GB VRAM-a. 

---


## :clipboard: Uporaba:
Ko si uporabnik naloži repozitorij z izvorno kodo, lahko sam izvede evalvacijo, ki je opisana v 4. poglavju diplomske naloge. To lahko stori tako, da preprosto požene datoteko [Main.java](Main.java) in sledi navodilom v ukazni vrstici. V mapi [test_trees](test_trees) se nahajajo testne množice vseh treh tipov drvesnih struktur. Če želi uporabnik generirati nove testne množice, to stori tako, da požene datoteko [TreeGenerator.java](TreeGenerator.java) in prav tako preko ukazne vrstice izbere za katere tipe dreves želi zgenerirati nove testne množice. V primeru, pa da si uporabik želi samo ogledati grafične rezultate evalvacije, ki se je nazadnje izvedla in katere meritve rezultati so shranjeni v mapi [test_results](test_results), pa lahko to stori tako, da požene datoteko **LCAChart.java** in izbere za katero vrsto dreves želi grafično prikazati rezultate.
---

## :mag_right: Opis datotek:
V nadaljevanju so opisane posamezne datoteke, ki so razdeljene v sledeče skupine: 

- :yellow_square: [Pomožne datoteke](#pomožne-datoteke),
- :red_square: [Naivna rešitev z uporabo rekurzije](#naivna-rešitev-z-uporabo-rekurzije) **&lt;O(N)&gt;**
- :blue_square:  [Rešitev z uporabo korenske dekompozicije drevesa](#rešitev-z-uporabo-korenske-dekompozicije-drevesa) **&lt;O(N), O($\sqrt{N}$)&gt;**,
- :green_square:   [Rešitev z uporabo tehnike binarnega dviga](#rešitev-z-uporabo-tehnike-binarnega-dviga), **&lt;O(N log N), O(log N)&gt;**
- :orange_square: [Rešitev s prevedbo problema LCA na RMQ](#rešitev-s-prevedbo-problema-lca-na-rmq), **&lt;O(N log N), O(1)&gt;**,
- :purple_square:  [Rešitev z Farach-Colton in Bender-jevim algoritmom](#red_square-rešitev-z-farach-colton-in-bender-jevim-algoritmom) **&lt;O(N), O(1)&gt;**

---

###  :yellow_square: Pomožne datoteke:

- [TreeNode.java](TreeNode.java) razred predstavlja vozlišče v drevesu. Razred *TreeNode* je serializiran, kar omgoča pretvorbo v zaporedno obliko za shranjevanje in prenos podatkov. Razred vsebuje naslednje atribute in metode:

    Atributi:
    + **int value** &rarr; hrani vrednost vozlišča.
    + **int depth** &rarr; hrani globino vozlišča.
    + **TreeNode parent** &rarr; hrani starša vozlišča.
    + **List &lt;TreeNode&gt; children** &rarr; je seznam, ki hrani vse otroke tega vozlišča.
    
    <br>

    Metode:
    + Konstruktor *TreeNode* ustvari novo vozlišče s podano vrednostjo in globino. 
    + **public void addChild(TreeNode child)** &rarr; metoda doda otroka vozlišču.
    + **public int getValue()** &rarr; "*getter*" metoda, ki omogoča dostop do vrednosti vozlišča.
    + **public int getDepth()** &rarr; "*getter*" metoda, ki omogoča dostop do globine vozlišča.

--- 

- [TreeGenerator.java](TreeGenerator.java) razred vključuje metode za ustvarjanje dreves, izpis posameznih dreves, ter za shranjevanje in branje drevesnih struktur v datoteke. Vse spodaj navedene metode za generiranje dreves, kličejo privatne metode z istim imenom, ki rekurzivno generirajo drevesne strukture in vrnejo njihov koren.

    Razred vsebuje naslednje metode:
    + **public TreeNode generateRandomTree(int depth, int maxChildren)** &rarr; metoda ustvari **naključno drevo** s podano globino (*depth*) in največjim številom otrok za posamezno vozlišče (*maxChildren*). Metoda vrne koren tega drevesa.
    + **public TreeNode generateCompleteTree(int depth, int numChildren)** &rarr; metoda ustvari **popolno drevo** s podano globino (*depth*) in številom otrok za vsako vozlišče (*numChildren*). Metoda vrne koren tega drevesa.
    + **public TreeNode generateSkewedTree(int maxDepth)** &rarr; metoda ustvari **izrojeno drevo** s podano največjo globino (*depth*). Metoda vrne koren tega drevesa.
    + **public void saveTreeToFile(TreeNode root, String filename)** &rarr; metoda shrani drevo, ki ga predstavlja koren *root*, v datoteko z danim imenom *filename*. Uporablja serializacijo za shranjevanje drevesa v binarni obliki.
    + **public TreeNode loadTreeFromFile(String filename)** &rarr; metoda naloži drevo iz datoteke s podanim imenom *filename* in vrne koren tega naloženega drevesa. Uporablja deserializacijo za obnovitev drevesne strukture iz binarnih podatkov v datoteki.
     + **public void generateRandomTreeTestSet(int maxDepth, int step, int ,maxChildren)** &rarr; metoda ustvari množico naključnih dreves z globinami od (*step*) pa do maksimalne globine oz. višine drevesa podane z (*maxDepth*). Vsako vozlišče, ki ne predstavlja lista drevesa, ima število otrok med 1 in *maxChildren*. Vsako zgenerirano drevo je shranjeno v podmapo **test_trees/random_trees/** kot *random_tree_i_.ser*,  kjer *i* predstavlja globino oz. višino drevesa. 
    + **public void generateCompleteTreeTestSet(int maxDepth, int step, int numChildren)** &rarr; metoda ustvari množico popolnih dreves z globinami od (*step*) pa do maksimalne globine oz. višine drevesa podane z (*maxDepth*). Vsako vozlišče, ki ne predstavlja lista drevesa, ima število otrok enako *numChildren*. Vsako zgenerirano drevo je shranjeno v podmapo **test_trees/complete_trees/** kot *complete_tree_i_.ser*,  kjer *i* predstavlja globino oz. višino drevesa. 
    + **public void generateSkewedTreeTestSet(int maxDepth, int step)** &rarr; metoda ustvari množico izrojenih dreves z globinami od (*step*) pa do maksimalne globine oz. višine drevesa podane z (*maxDepth*). Vsako vozlišče, ki ne predstavlja lista drevesa, ima natanko enega otroka. Vsako zgenerirano drevo je shranjeno v podmapo **test_trees/skewed_trees/** kot *skewed_tree_i_.ser*,  kjer *i* predstavlja globino oz. višino drevesa. 



---

- [UtilMethods.java](UtilMethods.java) razred vključuje pomožne metode, ki se uporabljajo v različnih razredih:
    
    + **public static int getNumberOfNodes(TreeNode root)** &rarr; statična metoda, ki vrne število vozlišč v drevesu za drevo podano s korenom *root*.
    + **public static int height(TreeNode root)** &rarr; statična metoda, ki vrne višino poljubnega vozlišča. V nalogi se največkrat uporablja za izračun višine drevesa podanega s korenom *root*.
    + **public static TreeNode[] swapNodes(TreeNode node1, TreeNode node2)** &rarr; statična metoda, ki vrne tabelo z *node1* in *node2*, kar omogoča zamenjavo vozlišč.
    + **public static void deleteExistingFiles(String[] fileNames)** &rarr; statična metoda ki izbriše obstoječe datoteke, ki so podane kot seznam poti do datotek, ki jih želimo izbrisati.
    + **public static void(storeAverageTime)** &rarr; statična metoda, ki zapiše povprečen rezultat meritev testiranja podan z *averageTime* v datoteko z imenom *fileName*.


---

### :red_square: Naivna rešitev z uporabo rekurzije:


- [LCA_recursion.java](LCA_recursion.java) razred, predstavlja prvi način iskanja najnižjega skupnega prednika (LCA) dveh vozlišč v drvesu. V tem primeru se uporabi enostaven rekurzivni pristop. Časovna zahtevnost odgovarjanja na poizvedbe pa je **O(N)**, kjer je N število vozlišč v drevesu.  
Razred vsebuje 2 metodi:

    + **private boolean findPath(TreeNode current, int target, ArrayList &lt;TreeNode&gt; path)** &rarr; metoda sprejme začetno vozlišče *current* (po navadi koren drevesa) in pa vrednost vozlišča, ki ga želimo poiskati. Metoda za iskanje vozlišča izvede iskanje v globino (*depth first search*) in pot do vozlišča shranjuje v ArrayList *path*. Če je vozlišče s podano vrednostjo *target* najdeno, metoda vrne *true*, v nasprotnem primeru, pa vrne *false*.
    + **public TreeNode getLCA(TreeNode root, int node1_value, int node2_value)** &rarr; metoda sprejme koren drevesa *root*, v katerem bomo iskali najnižjega skupnega prednika, ter vrednosti *node1_value* in *node2_value*, ki predstavljata vrednosti vozlišč, za kateri želimo poiskati najnižjega skupnega prednika. Metoda najprej inicializira dva ArrayLista, kamor bomo s pomočjo metode *findPath* shranili pot do vozlišč z vrednostima *node1_value* in *node2_value*. V primeru, da s pomočjo funkcije *findPath* ne najdemo enega ali pa obeh vozlišč, metoda vrne ustrezno izjemo. V nasprotnem primeru pa se s *for* zanko sprehodimo čez elemnte ArrayListov in ko se vozlišča na istem indeksu razlikujeta, pomeni da je vozlišče na prejšnjem indeksu najnižji skupni prednik (LCA) danih vozlišč.

---

### :blue_square: Rešitev z uporabo korenske dekompozicije drevesa:

- [LCA_sqrt.java](LCA_sqrt.java) razred, predstavlja drugi način iskanja najnižjega skupnega prednika (LCA) dveh vozlišč v drevesu. Ta način predstavlja izboljšavo v primerjavi s prvim, saj s pomočjo **korenske dekompozicije drevesa** doseže bolj optimalno časovno zahtevnost odgovarjanja na poizvedbe. Časovna zahtevnost predprocesiranja je **O(N)**, časovna zahevnost odgovarjanja na poizvedbe pa je **O($\sqrt{N}$)**, kjer je N število vozlišč v drevesu.

    Atributi:
    + **private long preprocessTime** &rarr; predstavlja čas predprocesiranja, ki ga algoritem porabi za predprocesiranje drevesa podanega s korenom *root*.
    + **TreeNode root** &rarr; predstavlja korensko vozlišče drevesa, na katerem se bo izvajal algoritem za iskanje najnižjega skupnega prednika.
    + **int numNodes** &rarr; predstavlja število vozlišč v danem drevesu.
    + **int blockSize** &rarr; predstavlja velikost posameznega bloka v korenski dekompoziciji drevesa. Izračuna se kot kvadratni koren višine drevesa.
    + **TreeNode jumpParents** &rarr; predstavlja tabelo vozlišč, ki za vsako vozlišče hrani referenco starša, ki je najbližje temu vozlišču in se hkrati nahaja v bloku, ki je nad blokom, v katerem se nahaja to vozlišče.  Tem staršem bomo v nadaljevanju rekli preskočni starši. 
    + **Map&lt;Integer, TreeNode&gt;** &rarr; predstavlja slovar, ki ima za ključe vrednosti vozlišč, vrednosti pa predstavljajo kazalce oz. reference na vozlišča s temi vrednostmi. Ta slovar se uporablja, ker uporabnik v ukazno vrstico vnese vrednosti vozlišč, za kateri želi poiskati najnižjega skupnega prednika. Slovar pa potrebujemo zato, da za pripadajoče dobimo pripadajoča vozlišča, kar omogoča izvajanje algoritma.

    <br>

    Konstruktor:
    + **public LCA_sqrt(TreeNode root)** &rarr; sprejme koren drevesa *root* in nastavi vrednosti atributom razreda, tako da s pomočjo pomožnih metod *getNumberOfNodes()* in *height()* iz razreda *UtilMethods* dobi število vozlišč in višino danega drevesa. Število vozlišč se uporabi tudi za incializacijo tabele vozlišč TreeNode *jumpParents*. Konstruktor prav tako pokliče metodo *getJumpParents*, ki napolni tabelo preskočnih staršev *jumpParenst* in slovar *nodeMap*. Znotraj konstruktorja se izvede tudi merjenje časa predprocesiranja. To vključuje nastavitev vrednosti in inicializacijo vseh atributov razreda, ter izvedba metode *getJumpParents()*. 


    <br>

    Metode:
    + **private void getJumpParents(TreeNode node)** &rarr; metoda izvede iskanje v globino (*depth first search*) in za vsako vozlišče shrani satrša, ki je temu vozlišču najbližje in je hkrati v bloku, ki je nad blokom v katerem se nahaja to vozlišče.  Hkrati pa shranjuje pare vrednosti in pripadajočih vozlišč v slovar *nodeMap*. 

    + **public TreeNode getLCA(int node1_value, int node2_value)** &rarr; metoda sprejme vrednosti *node1_value* in *node2_value*, ki predstavljata vrednosti vozlišč, za kateri želimo poiskati najnižjega skupnega prednika. S pomočjo slovarja *nodeMap* pridobimo kazalce na vozlišči s podanimi vrednostmi. V primeru, da podanih vrednosti ni v slovarju, metoda vrne ustrezno izjemo. Nato pa za dani vozlišči primerjamo njuna starša v naslednjih blokih (*jumpParent*). Dokler ti dve vozlišči nimata istega *jumpParenta*, dvigujemo vozlišče *node1* tako, da z spremenljivko *node1* pokažemo na njegovega starša v naslednjem bloku &rarr; torej naredimo skok, ki je lahko velik tudi več nivojev. Pri tem predpostavimo, da je vozlišče *node1* tisto, ki je globlje, v nasprotnem primeru, pa vozlišči s pomočjo pomožne metode *swapNodes* zamenjamo. Ko imata vozlišči istega *jumpParenta*, to pomeni, da sta v istem bloku. Nato ju po potrebi spravimo na enako globino in nato "plezamo" proti korenu tako, da z spremenljvikama *node1* in *node2* pokažemo na direktnega starša posameznega vozlišča. Ko z *node1* in *node2* kažemo na isto vozlišče, smo našli najnižjega skupnega prednika, ki ga metoda tudi vrne.

    + **public long getPreprocessTime()** &rarr; metoda vrne vrednost privatnega atributa *preprocessTime*.


---

### :green_square: Rešitev z uporabo tehnike binarnega dviga:

- [LCA_binary_lift.java](LCA_binary_lift.java) razred, predstavlja tretji način iskanja najnižjega skupnega prednika (LCA) dveh vozlišč v drevesu. Ta način predstavlja s pomočjo **hranjenja *log N* prednikov za vsako vozlišče** in uporabo teh za večje premike (skoke) po drvesu doseže še bolj optimalno časovno zahtevnost odgovarjanja na LCA poizvedbe. Časovna zahtevnost predprocesiranja se poveča in znaša  **O(N log N)**, časovna zahevnost odgovarjanja na poizvedbe pa je **O(log N)**, kjer je N število vozlišč v drevesu. 

    Atributi:
    + **private long preprocessTime** &rarr; predstavlja čas predprocesiranja, ki ga algoritem porabi za predprocesiranje drevesa podanega s korenom *root*.
    + **TreeNode root** &rarr; korensko vozlišče drevesa.
    + **TreeNode[][] ancestors** &rarr; tabela, v kateri za vsakega od vozlišč hranimo določeno število referenc prednikov. **ancestors[i][j]** predstavlja prednika, ki je od i-tega vozlišča oddaljen 2^j nivojev.
    + **int numNodes** &rarr; število vozlišč v danem drevesu.
    + **int numAncestors** &rarr; število prednikov, ki jih hranimo za vsako vozlišče. Izračuna se kot dvojiški logaritem višine danega drevesa.
    + **Map&lt;Integer, TreeNode&gt;** &rarr; slovar, ki ima za ključe vrednosti vozlišč, vrednosti pa predstavljajo kazalce na vozlišča s temi vrednostmi. Ta slovar se uporablja, ker uporabnik v ukazno vrstico vnese vrednosti vozlišč, za kateri želi poiskati najnižjega skupnega prednika. Slovar pa potrebujemo zato, da za pripadajoče dobimo pripadajoča vozlišča, kar omogoča izvajanje algoritma.

    <br>

    Konstruktor:
    + **public LCA_binary_lift(TreeNode root)** &rarr; sprejme koren drevesa *root* in nastavi vrednosti atributom razreda, tako da s pomočjo pomožnih metod *getNumberOfNodes()* in *height()* iz razreda *UtilMethods* dobi število vozlišč in višino danega drevesa. Višina se uporabi, za izračun števila prednikov, ki jih bomo hranili za vsako vozlišče. Izračuna se kot dvojiški logaritem višine danega drevesa. Prav tako se incializira tabela prednikov *ancestors*. Konstruktor prav tako pokliče metodo *getAncestorsArray()*, ki napolni tabelo prednikov *ancestors* in slovar *nodeMap*. Znotraj konstruktorja se izvede tudi merjenje časa predprocesiranja. To vključuje nastavitev vrednosti in inicializacijo vseh atributov razreda, ter izvedba metode *getAncestorsArray()*.

    <br>

    Metode:
    + **private void getAncestorArray(TreeNode root)** &rarr; metoda izvede iskanje v globino (*depth first search*) in za vsako vozlišče shrani *numAncestors* prednikov. Za vsako vozlišče se shranijo predniki, ki so od vozlišča oddaljeni za potence števila 2. Torej, če hranimo 3 prednike za vsako vozlišče, to pomeni, da hranimo prednike ki so 2^0, 2^1 in 2^2 nivojev nad tem vozliščem. Hkrati pa shranjuje pare vrednosti in pripadajočih vozlišč v slovar *nodeMap*.  

    + **public TreeNode getLCA(int node1_value, int node2_value)** &rarr; metoda sprejme vrednosti *node1_value* in *node2_value*, ki predstavljata vrednosti vozlišč, za kateri želimo poiskati najnižjega skupnega prednika. S pomočjo slovarja *nodeMap* pridobimo kazalce na vozlišči s podanimi vrednostmi. V primeru, da podanih vrednosti ni v slovarju, metoda vrne ustrezno izjemo.  
    Predpostavimo, da je vozlišče *node1* tisto, ki je globje, v nasprotnem primeru, pa vozlišči s pomočjo pomožne metode *swapNodes* zamenjamo.  Nato izračunamo razliko v globini med vozliščema in dokler vozlišči nista na istem nivoju, dvigujemo vozlišče, ki je globje - torej *node1*. To storimo tako, da razliko v globini zapišemo v dvojiškem zapisu in iz tega vzamemo  najvišji (najbolj levi) bit, ki je enak 1, in mesto, kjer je ta enica, predstavlja največji skok, ki ga lahko naredimo za vozlišče *node1*. Npr. razlika v viši za 2 vozlišči je 4 &rarr; dvojiško se to zapiše kot 0100 &rarr; najvišji bit, ki je enak 1 je bit na *drugem mestu* - to pomeni, da lahko z vozliščem *node1* "skočimo" na mesto prednika, ki  je 2^2 nivojev nad našim vozliščem.  
    Ko imamo vozlišči na enaki višini, pa se s pomočjo for zanke sprehodimo čez vse prednike obeh volišč. Če imata vozlišči različna prednika na določeni višini, "skočimo" na mesto teh prednikov. Po končani zanki se vozlišči nahajata točno en nivo pod njunim skupnim prednikom, tako da je najnižji skupni prednik kar neposreden starš kateregakoli od vozlišč, ki ga metoda tudi vrne.

    + **public long getPreprocessTime()** &rarr; metoda vrne vrednost privatnega atributa *preprocessTime*.


---

### :orange_square: Rešitev s prevedbo problema LCA na RMQ:


- [LCA_RMQ.java](LCA_RMQ.java) razred, predstavlja četrti način iskanja najnižjega skupnega prednika (LCA) dveh vozlišč v drevesu. Tukaj, problem iskanja najnižjega skupnega prednika (LCA) prvedemo na iskanje najmanjše vrednosti na intervalu (RMQ). Časovna zahtevnost predprocesiranja je **O(N log N)**, časovna zahevnost odgovarjanja na poizvedbe pa je konstantna **O(1)**.

    Atributi:
    + **private long preprocessTime** &rarr; predstavlja čas predprocesiranja, ki ga algoritem porabi za predprocesiranje drevesa podanega s korenom *root*.
    + **TreeNode root** &rarr; korensko vozlišče drevesa.
    + **int numNodes** &rarr;  število vozlišč v drevesu.
    + **ArrayList&lt;Integer&gt;** eulerTourArray &rarr; Eulerjev obhod drevesa.
    + **ArrayList&lt;Integer&gt;** depthArray &rarr; pripadajoče globine na Eulerjevem obhodu drevesa.
    + **int[] firstAppearanceIndex** &rarr; tabela, ki vsebuje index prve pojavitve posamezenega vozlišča v Eulerjevem obhodu.
    + **int[] log2Array** &rarr; tabela, ki vsebuje vnaprej izračunane vrednosti dvojiških logaritmov.
    + **int[] pow2Array** &rarr; tabela, ki vsebuje vnaprej izračunane vrednosti potenc števila dve.
    + **int[][] sparseTable** &rarr; tabela hrani indekse odgovorov na vse možne poizvedbe o najmanjši vrednosti na intervalu, pricemer je dolžina intervala potenca števila dve.

     <br>

    Konstruktor:
    + **public LCA_RMQ(TreeNode root)** &rarr; sprejme koren drevesa *root* in nastavi vrednosti atributom razreda oziroma jih inicializira. Pri tem uporabi pomožno funkcijo *getNumberOfNodes()* iz razreda *UtilMethods*, ki vrne število vozlišč danega drevesa. Konstruktor prav tako pokliče vse metode, ki poskrbijo za predprocesiranje drevesa - *buildHelperArrays()*, *dfs(TreeNode root)* in pa *buildSparseTable()*. Znotraj konstruktorja se izvede tudi merjenje časa predprocesiranja. To vključuje nastavitev vrednosti in inicializacijo vseh atributov razreda, ter izvedbe metod, ki skrbijo za predprocesiranje drevesa.

    <br>

    Metode:
    + **private void buildHelperArrays()** &rarr; zapolni tabeli *log2Array* in *pow2Array* z vrednostmi dvojiških logaritmov in potenc števila dve za dani indeks.

    + **private void dfs(TreeNode root)** &rarr: izvede iskanje v globino in s tem Eulerjev obhod drevesa, ter napolni tabele *eulerTourArray*,  *depthArray* in *firstAppearanceIndex*.

    + **private void buildSparseTable()** &rarr; zgradi tabelo z uporabo dinamičnega programiranja. Tabela shranjuje indekse vozlišč z najmanjšo globino za vse možne razpone dolžin, ki so potence števila dve, kar omogoča hitro iskanje najmanjše vrednosti na določenem intervalu. Na mestu **sparseTable[i][j]** se nahaja indeks najmanjše vrednosti na intervalu [j, j + 2^i) iz vhodne tabele. 

    + **private int query(int left, int right)** &rarr; naredi poizvedbo nad *sparseTable[][]* in vrne indeks najmanjše vrednost iz tabele globin na danem intervalu.

    + **public TreeNode getLCA(int node1_value, int node2_value)** &rarr; metoda sprejme vrednosti *node1_value* in *node2_value*, ki predstavljata vrednosti vozlišč, za kateri želimo poiskati najnižjega skupnega prednika. V primeru, da podane vrednosti ne obstajajo, metoda vrne ustrezno napako. Nato metoda na podlagi indeksov prve pojavitve v Eulerjevem obhodu določi levi (*left*) in desni (*right*) indeks intervala. Ta dva indeksa uporabi v metodi *query()*, ki vrne indeks elementa z najmanjšo globino na tem intervalu iz tabele *eulerTourArray*. Nato s pomočjo tega indeksa dobi kazalec na vozlišče, ki predstavlja najnižjega skupnega prednika za dani vozlišči.

    + **public long getPreprocessTime()** &rarr; metoda vrne vrednost privatnega atributa *preprocessTime*.



--- 


### :purple_square: Rešitev z Farach-Colton in Bender-jevim algoritmom:

- [LCA_FCB.java](LCA_FCB.java) razred, predstavlja peti in hkrati **najbolj učinkovit** način iskanja najnižjega skupnega prednika (LCA) dveh vozlišč v drevesu. Datoteka vsebuje implementacijo Farach-Colton in Bender-jevega algoritma. Časovna zahtevnost predprocesiranja je linearna **O(N)**, časovna zahevnost odgovarjanja na poizvedbe pa je konstantna **O(1)**.
    
    Atributi:
    + **private long preprocessTime** &rarr; predstavlja čas predprocesiranja, ki ga algoritem porabi za predprocesiranje drevesa podanega s korenom *root*.
    + **TreeNode root** &rarr; korensko vozlišče drevesa.
    + **int numNodes** &rarr;  število vozlišč v drevesu.
    + **int p** &rarr; določa število vrstic v *sparseTable*.
    + **int blockSize** &rarr; določa velikost posameznega bloka. 
    + **int numBlocks** &rarr; število blokov na katero razdelimo *depthArray*.
    + **int eulerTourSize &rarr; dolžina Eulerjevega obhoda.
    + **ArrayList&lt;Integer&gt;** eulerTourArray &rarr; Eulerjev obhod drevesa.
    + **ArrayList&lt;Integer&gt;** depthArray &rarr; pripadajoče globine na Eulerjevem obhodu drevesa.
    + **int[] firstAppearanceIndex** &rarr; tabela, ki vsebuje index prve pojavitve posamezenega vozlišča v Eulerjevem obhodu.
    + **int[] log2Array** &rarr; tabela, ki vsebuje vnaprej izračunane vrednosti dvojiških logaritmov.
    + **int[] pow2Array** &rarr; tabela, ki vsebuje vnaprej izračunane vrednosti potenc števila dve.
    + **int[][] sparseTable** &rarr; tabela, ki vsebuje najmanjše vrednosti za vse možne razpone dolžin, ki so potence števila dve.
    + **int[] minOfEachBlock** &rarr; tabela, ki vsebuje vnaprej izračunane indekse elementov z najmanjšo globino v posameznem bloku.
    + **int[] blockStartingIndex** &rarr; tabela, ki za vsak blok hrani index, s katerim se pripadajoči blok začne.
    + **int[] blockBitmasks** &rarr; tabela, ki vsebuje vnaprej izračunane bitne maske za posamezen blok.
    + **int[][][] precomputedBlocks** &rarr; tabela, ki vsebuje indekse najnižjih skupnih prednikov za vse možne podintervale znotraj posameznega bloka.

    <br>

    Konstruktor:
    + **public LCA_FCB(TreeNode root)** &rarr; sprejme koren drevesa *root* in nastavi vrednosti atributom razreda oziroma jih inicializira. Pri tem uporabi pomožno funkcijo *getNumberOfNodes* iz razreda *UtilMethods*, ki vrne število vozlišč danega drevesa. Konstruktor prav tako pokliče vse metode, ki poskrbijo za predprocesiranje drevesa - *buildHelperArrays()*, *dfs(TreeNode root)*, *getMinOfEachBlock(int blockSize)*, *buildSparseTable()*, *precomputeBlockBitMasks()* in *precomputedBlocks()*. Znotraj konstruktorja se izvede tudi merjenje časa predprocesiranja. To vključuje nastavitev vrednosti in inicializacijo vseh atributov razreda, ter izvedbe metod, ki skrbijo za predprocesiranje drevesa.

    <br>

    Metode:
    + **private void buildHelperArrays()** &rarr; zapolni tabeli *log2Array* in *pow2Array* z vrednostmi dvojiških logaritmov in potenc števila dve za dani indeks.

    + **private void dfs(TreeNode root)** &rarr: izvede iskanje v globino in s tem Eulerjev obhod drevesa, ter napolni tabele *eulerTourArray*,  *depthArray* in *firstAppearanceIndex*.

    + **private void getMinOfEachBlock(int blockSize)** &rarr; sprejme parameter *blockSize*, ki določa velikost posameznega bloka *depthArraya* in za vsak blok v *depthArray* poišče element z najmanjšo globino in njegov indeks shrani v tabelo *minOfEachBlock*.

    + **private void buildSparseTable()** &rarr; zgradi tabelo na podlagi tabele *minOfEachBlock* z uporabo dinamičnega programiranja. Tabela shranjuje indekse vozlišč z najmanjšo globino za vse možne razpone dolžin, ki so potence števila dve, kar omogoča hitro iskanje najmanjše vrednosti na določenem intervalu.

    + **private void precomputedBlockBitMasks()** &rarr; metoda vnaprej izračuna bitne maske za posamezen blok. Vsak bit maske predstavlja smer spremembe globine med dvema zaporednima vozliščema v bloku, kjer 1 označuje naraščanje in 0 označuje zmanjšanje ali enakost.

    + **private void precomputeBlocks()** &rarr; metoda vnaprej izračuna LCA za vse možne podintervale posameznega bloka in njihove indekse shrani v tabelo **precomputedBlocks[blockMask][start][end]**, kjer *blockMask* predstavlja bitnoMasko bloka, *start* levi indeks intervala in *right* desni indeks podintervala. Ker ima lahko več blokov enako bitno masko in bo rezultat v tem primeru enak, izračune izvedemo tolikokrat, kolikor je različnih bitnih mask.

    + **public TreeNode getLCA (int node1_value, int node2_value)** &rarr;  metoda sprejme vrednosti *node1_value* in *node2_value*, ki predstavljata vrednosti vozlišč, za kateri želimo poiskati najnižjega skupnega prednika. V primeru, da podane vrednosti ne obstajajo, metoda vrne ustrezno izjemo. Nato metoda določi indekse blokov, v katerih se nahajata prvi pojavitvi iskanih vozlišč - *leftBlockIndex* in *rightBlockIndex*. 
    Ločimo dva scenarija:
        + Če velja *leftBlockIndex* == *rightBlockIndex*, to pomeni, da vozlišči ležita znotraj istega bloka. LCA se v tem primeru določi neposredno iz tabele *precomputedBlocks*, ki vsebuje indekse vseh LCA-jev za vse možne podintervale znotraj posameznega bloka. 
        + Če pa se vozlišči nahajata v različnih blokih, moramo izračunati tri indekse:
            - Indeks najmanjše globine od začetka intervala (označeno z *left*) do konca bloka z indeksom *leftBlockIndex* &rarr; uporabimo tabelo *precomputedBlocks*.
            - Indeks najmanjše globine vmesnih blokov (če so prisotni) med blokoma z indeksoma *leftBlockIndeks* in *rightBlockIndex* &rarr; uporabimo *sparseTable*
            - Indeks najmanjše globine od začetka bloka z indeksom *rightBlockIndex* pa do konca intervala, ki ga označuje *right* &rarr; uporabimo tabelo *precomputedBlocks*
            Sledi primerjava globin na treh (ali pa dveh, če sta bloka z indeksoma *leftBlockIndex* in *rightBlockIndex* sosednja) indeksih. Indeks z najmanjšo globino predstavlja LCA v tabeli eulerTourArray. Metoda vrne vozlišče, ki se nahaja na tem mestu, kot rezultat LCA za dani vozlišči.

    + **public long getPreprocessTime()** &rarr; metoda vrne vrednost privatnega atributa *preprocessTime*.

--- 

## :white_check_mark: Evalvacija:

Celotna evalvacija se izvede v datoteki [Main.java](Main.java). Glede na tip drevesa, ki ga izbere uporabnik preko ukazne vrstice, se izvede evalvacija za izbrano vrsto drevesa. Za vsakega od peteih algoritmov je napisana svoja metoda za tesitranje predprocesiranja in odgovarajnanja na poizvedbe, saj imajo algoritmi različne kompleksnosti in imajo posledično različne parametre testiranja. 

### Predprocesiranje: 

Korak predprocesiranja programsko evalviramo tako, da zgeneriramo določeno število dreves, pri čemer je vsako naslednje drevo globlje in ima posledično večje število vozlišč. Nato korak predprocesiranja na vsakem izmed teh dreves, za vsak algoritem, odvisno od njegove časovne zahtevnosti, večkrat ponovimo. V vsaki iteraciji shranimo izmerjen čas predprocesiranja v tabelo in na koncu vse skupaj povprečimo. Ker so implementacije napisane v programskem jeziku Java, je potrebno upoštevati tudi lastnosti JVM (angl. Java Virtual Machine), ki računalniku omogoča izvajanje javanskih programov. Ko zaženemo program, se običajno zgodi, da JVM opravlja nekatere inicializacijske naloge, ki so lahko časovno potratne in vplivajo na hitrost izvajanja programa v nekaj začetnih iteracijah. Da zagotovimo konsistentne in kakovostne meritve, začnemo čas izvajanja predprocesiranja meriti šele po nekaj začetnih iteracijah.  

Na evalvacijo lahko prav tako vplivajo drugi dejavniki, kot na primer hitrost procesorja, temperatura procesorja, število procesorskih jeder, operacijski sistem in izvajanje drugih aplikacij na računalniku (brskalnik, predvajalnik glasbe ipd.) ki porabljajo procesorske in pomnilniške vire. Visoka temperatura procesorja lahko privede do zmanjšane frekvence delovanja procesorja (angl. thermal throttling) ali celo vmesnih prekinitev. Do pregrevanja pride pri intenzivnih izračunih, ki močno obremenjujejo CPU. V sklopu te evalvacije so prisotna tudi drevesa z več kot 1.000.000 vozlišči, kar je povzročilo pregrevanje računalnika, na katerem se je izvajala evalvacija, opisana v tem poglavju. Posledično lahko zaradi teh vzrokov včasih naletimo na izstopajoče meritve (angl. outliers), ki močno vplivajo na povprečen čas predprocesiranja posameznega drevesa. Zato takšne primere iz tabele odstranimo, iz preostalih meritev pa izračunamo povprečen čas predprocesiranja za dano drevo in ga shranimo v datoteko. Za detekcijo ekstremnih primerov si pomagamo s sledečimi metodami:

+ **private static List<Long> removeOutliers(List<Long> times, double deviation)** &rarr; metoda sprejme seznam meritev *times* in pa standardni odklon *deviation*. Najprej preveri, če je podani seznam prazen. Če je prazen vrne izjemo, v nasprotnem primeru pa na podlagi časov v seznamu *times* izračuna povprečen čas s pomočjo metode *calculateAverage(). Nato metoda iterira čez vse meritve iz seznama *times* in preveri, ali posamezna meritev močno odstopa od povprečja  s pomočjo metode *isOutlier()*. Če **ne** gre za izstopajočo meritev, potem se ta meritev doda v nov seznam meritev, ki ga metoda tudi vrne. 

+ **private static long calculateAverage(List<Long> times)** &rarr; metoda na podlagi meritev iz seznama *times* izračuna in vrne povprečen čas teh meritev.

+ **private static boolean isOutlier(long currentValue, double average, double deviation)** &rarr; metoda preverja, ali je dana vrednost *currentValue* izstopajoča meritev glede na povprečje *average* in standardni odklon *deviation*. Povprečje in standardni odklon se uporabita za določitev spodnjega in zgornjega praga, med katerima stojijo običajne vrednosti. Če se trenutna vrednost nahaja med obema pragoma, potem **ne** gre za izstopajočo meritev in metoda vrne vrednost **false**. V primeru, pa da se trenutna vrednost nahaja izven tega območja, pa gre za izstopajočo meritev in metoda vrne vrednost **true**. 


### Odgovarjanje na LCA poizvedbe:

Korak odgovora na LCA poizvedbo programsko evalviramo tako, da za vsako drevo naredimo veliko naključnih LCA poizvedb, ki variirajo od 10.000 za naivni algoritem pa vse do 2.000.000 za algoritme z časovno zahtevnostjo odgovora na LCA poizvedbe O(1). Število poizvedb je torej odvisno od učinkovitosti posameznega algoritma. V tem primeru izvajanje odgovora na poizvedbe za posamezen algoritem izmerimo tako, da merimo izvajanje celotne zanke, znotraj katere se izvedejo poizvedbe. Tako kot pri evalvaciji predprocesiranja, tudi tukaj upoštevamo lastnosti JVM (angl. Java Virtual Machine) in časovnik zaženemo šele po nekaj začetnih iteracijah, da zagotovimo bolj konsistentne in kakovostne meritve.

Za razliko od evalvacije predprocesiranja, v tem primeru ne moremo izločiti nobenih izstopajočih meritev, saj pri metodah, ki nimajo konstantne časovne zahtevnosti odgovora na LCA poizvedbo, pričakujemo, da se bodo meritve za različne naključne poizvedbe kar precej razlikovale, saj je za različne poizvedbe potrebnih različno število operacij. Na koncu izmerjen čas izvajanja zanke delimo z številom poizvedb in tako dobimo povprečen čas odgovora na LCA poizvedbo za dano drevo.


## Pravilnost odgovorov na LCA poizvedbe

Ker imajo algoritmi različne zahtevnosti predprocesiranja in odgovarjanja na LCA poizvedbe, za svojo evalvacijo potrebujejo različne nastavitve parametrov. Zato smo za evalvacijo obeh korakov za vsak algoritem implementirali ločeni metodi. To nam omogoča, da vsak del algoritma testiramo ločeno za vsako metodo posebej. Slabost tega pristopa pa je preverjanje pravilnosti odgovorov na LCA poizvedbe, saj se evalvacije za vsako metodo izvajajo ločeno, hkrati pa je različno tudi število poizvedb, ki je odvisno od časovne zahtevnosti odgovora na LCA poizvedbo posameznega algoritma. Zato smo implementirali dodatno metodo **testQueryAnswersMatch()**, ki uporabi isto množico dreves, ki smo jo uporabili v prejšnjih korakih evalvacije in za vsako drevo izvede 500 naključnih LCA poizvedb, pri čemer uporabi vseh pet algoritmov. Vsi odgovori na poizvedbe se zapišejo v datoteko. V vsaki iteraciji se prav tako izvede primerjanje odgovorov vseh algoritmov in v primeru, da se ti ne ujemajo, program prekine z izvajanjem in javi napako.

--- 


## :chart_with_upwards_trend: Grafični prikaz rezultatov:

V datoteki [LCAChart.java](LCAChart.java) se nahaja izvorna koda, ki na podlagi rezultatov, ki se nahajajo v mapi **test_results** izriše grafe, ki omogočajo primerjavo glede na učinkovitost predprocesiranja in odgovorov na LCA poizvedbe vseh petih algoritmov. Koda je dobro pokomentirana in lahko razumljiva, zato je na tem mestu ne bomo podrobno predstavili.

---




