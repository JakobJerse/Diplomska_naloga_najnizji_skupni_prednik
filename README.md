# Diplomska naloga - Iskanje najnižjega skupnega prednika vozlišč v drevesu 
Repozitorij vsebuje izvorno kodo diplomske naloge iskanja najnižijega skupnega prednika vozlšč v drevesu.


## Uporaba:
Ko si uporabnik naloži repozitorij z izvorno kodo, preprosto požene datoteko **Main.java** in sledi navodilom v ukazni vrstici.


## Opis datotek:
Repozitorij vsebuje sledeče datoteke:

- **TreeNode.java** razred predstavlja vozlišče v drevesu. Razred *TreeNode* je serializiran, kar omgoča pretvorbo v zaporedno obliko za shranjevanje in prenos podatkov. Razred vsebuje naslednje atribute in metode:

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

- **TreeGenerator.java** razred vključuje metode za ustvarjanje dreves, izpis posameznih dreves, ter za shranjevanje in branje drevesnih struktur v datoteke. Vse spodaj navedene metode za generiranje dreves, kličejo privatne metode z istim imenom, ki rekurzivno generirajo drevesne strukture in vrnejo njihov koren.

    Razred vsebuje naslednje metode:
    + **public TreeNode generateRandomTree(int maxDepth, int maxChildren)** &rarr; metoda ustvari **naključno generirano drevo** s podano največjo globino (*maxDepth*) in največjim številom otrok za posamezno vozlišče (*maxChildren*). Metoda vrne koren tega drevesa.
    + **public TreeNode generateCompleteTree(int maxDepth, int numChildren)** &rarr; metoda ustvari **popolno drevo** s podano največjo globino (*maxDepth*) in številom otrok za vsako vozlišče (*numChildren*). Metoda vrne koren tega drevesa.
    + **public TreeNode generateSkewedTree(int maxDepth)** &rarr; metoda ustvari **izrojeno drevo** s podano največjo globino (*maxDepth*). Vrne koren tega drevesa.
    + **public void printTree(TreeNode node)** &rarr; metoda izpiše drevo, ki ga predstavlja dani koren (*node*), tako da prikaže drevo v obliki drevesne strukture, ki omogoča pregledno razumevanje vozlišč in njihovih otrok.
    + **public void saveTreeToFile(TreeNode root, String filename)** &rarr; metoda shrani drevo, ki ga predstavlja koren *root*, v datoteko z danim imenom *filename*. Uporablja serializacijo za shranjevanje drevesa v binarni obliki.
    + **public TreeNode loadTreeFromFile(String filename)** &rarr; metoda naloži drevo iz datoteke s podanim imenom *filename* in vrne koren tega naloženega drevesa. Uporablja deserializacijo za obnovitev drevesne strukture iz binarnih podatkov v datoteki.


---

- **UtilMethods.java** razred vključuje pomožne metode, ki se uporabljajo v različnih razredih:
    
    + **public static int getNumberOfNodes(TreeNode root)** &rarr; statična metoda, ki vrne število vozlišč v drevesu s podanim korenom tega drevesa *root*.
    + **public static int height(TreeNode root)** &rarr; statična metoda, ki vrne višino danega vozlišča *node*.
    + **public static TreeNode[] swapNodes(TreeNode node1, TreeNode node2)** &rarr; statična metoda, ki vrne tabelo z *node1* in *node2*, kar omogoča zamenjavo vozlišč.


---

- **LCA_recursion.java** razred, predstavlja prvi način iskanja najnižjega skupnega prednika (LCA) dveh vozlišč v drvesu. V tem primeru se uporabi enostaven rekurzivni pristop. Časovna zahtevnost predprocesiranja in odgovarjanja na poizvedbe pa je **O(N)**, kjer je N število vozlišč v drevesu.  
Razred vsebuje 2 metodi:

    + **private boolean findPath(TreeNode current, int target, ArrayList &lt;TreeNode&gt; path)** &rarr; metoda sprejme začetno vozlišče *current* (po navadi koren drevesa) in pa vrednost vozlišča, ki ga želimo poiskati. Metoda za iskanje vozlišča izvede iskanje v globino (*depth first search*) in pot do vozlišča shranjuje v ArrayList *path*. Če je vozlišče s podano vrednostjo *target* najdeno, metoda vrne *true*, v nasprotnem primeru, pa vrne *false*.
    + **public TreeNode getLCA(TreeNode root, int node1_value, int node2_value)** &rarr; metoda sprejme koren drevesa *root*, v katerem bomo iskali najnižjega skupnega prednika, ter vrednosti *node1_value* in *node2_value*, ki predstavljata vrednosti vozlišč, za kateri želimo poiskati najnižjega skupnega prednika. Metoda najprej inicializira dva ArrayLista, kamor bomo s pomočjo metode *findPath* shranili pot do vozlišč z vrednostima *node1_value* in *node2_value*. V primeru, da s pomočjo funkcije *findPath* ne najdemo enega ali pa obeh vozlišč, metoda vrne ustrezno napako. V nasprotnem primeru pa se s *for* zanko sprehodimo čez elemnte ArrayListov in ko se vozlišča na istem indeksu razlikujeta, pomeni da je vozlišče na prejšnjem indeksu najnižji skupni prednik danih vozlišč.

---

- **LCA_sqrt** razred, predstavlja drugi način iskanja najnižjega skupnega prednika (LCA) dveh vozlišč v drevesu. Ta način predstavlja izboljšavo v primerjavi s prvim, saj s pomočjo **korenske dekompozicije drevesa** doseže bolj optimalno časovno zahtevnost. Časovna zahtevnost predprocesiranja je **O(N)**, časovna zahevnost odgovarjanja na poizvedbe pa je **O(sqrt(h))**, kjer je N število vozlišč v drevesu, h pa višina drevesa.

    Atributi:
    + **TreeNode root** &rarr; predstavlja korensko vozlišče drevesa, na katerem se bo izvajal algoritem za iskanje najnižjega skupnega prednika.
    + **int numNodes** &rarr; predstavlja število vozlišč v danem drevesu.
    + **int blockSize** &rarr; predstavlja velikost posameznega bloka v korenski dekompoziciji drevesa. Izračuna se kot kvadratni koren višine drevesa.
    + **TreeNode jumpParents** &rarr; predstavlja tabelo vozlišč, ki za vsako vozlišče hrani starša, ki je najbližje temu vozlišču in se hkrati nahaja v bloku, ki je nad blokom, v katerem se nahaja to vozlišče. 
    + **Map&lt;Integer, TreeNode&gt;** &rarr; predstavlja slovar, ki ima za ključe vrednosti vozlišč, vrednosti pa predstavljajo kazalce na vozlišča s temi vrednostmi. Ta slovar se uporablja, ker uporabnik v ukazno vrstico vnese vrednosti vozlišč, za kateri želi poiskati najnižjega skupnega prednika. Slovar pa potrebujemo zato, da za pripadajoče dobimo pripadajoča vozlišča, kar omogoča izvajanje algoritma.

    <br>

    Konstruktor:
    + **public LCA_sqrt(TreeNode root)** &rarr; sprejme koren drevesa *root* in nastavi vrednosti atributom razreda, tako da s pomočjo pomožnih metod *getNumberOfNodes* in *height* iz razreda *UtilMethods* dobi število vozlišč in višino danega drevesa. Število vozlišč se uporabi tudi za incializacijo tabele vozlišč TreeNode *jumpParents*. Konstruktor prav tako pokliče metodo *getJumpParents*, ki napolni tabelo staršev *jumpParenst* in slovar *nodeMap*.


    <br>

    Metode:
    + **private void getJumpParents(TreeNode node)** &rarr; metoda izvede iskanje v globino (*depth first search*) in za vsako vozlišče shrani satrša, ki je temu vozlišču najbližje in je hkrati v bloku, ki je nad blokom v katerem se nahaja to vozlišče.  Hkrati pa shranjuje pare vrednosti in pripadajočih vozlišč v slovar *nodeMap*. 

    + **public TreeNode getLCA(int node1_value, int node2_value)** &rarr; metoda sprejme vrednosti *node1_value* in *node2_value*, ki predstavljata vrednosti vozlišč, za kateri želimo poiskati najnižjega skupnega prednika. S pomočjo slovarja *nodeMap* pridobimo kazalce na vozlišči s podanimi vrednostmi. V primeru, da podanih vrednosti ni v slovarju, metoda vrne ustrezno napako. Nato pa za dani vozlišči primerjamo njuna starša v naslednjih blokih (*jumpParent*). Dokler ti dve vozlišči nimata istega *jumpParenta*, dvigujemo vozlišče *node1* tako, da z spremenljivko *node1* pokažemo na njegovega starša v naslednjem bloku &rarr; torej naredimo skok, ki je lahko velik tudi več nivojev. Pri tem predpostavimo, da je vozlišče *node1* tisto, ki je globlje, v nasprotnem primeru, pa vozlišči s pomočjo pomožne metode *swapNodes* zamenjamo. Ko imata vozlišči istega *jumpParenta*, to pomeni, da sta v istem bloku. Nato ju po potrebi spravimo na enako globino in nato "plezamo" proti korenu tako, da z spremenljvikama *node1* in *node2* pokažemo na direktnega starša posameznega vozlišča. Ko z *node1* in *node2* kažemo na isto vozlišče, smo našli najnižjega skupnega prednika, ki ga metoda tudi vrne.


---

- **LCA_binaryLift.java** razred, predstavlja tretji način iskanja najnižjega skupnega prednika (LCA) dveh vozlišč v drevesu. Ta način predstavlja še dodatno izboljšavo v primerjavi s prvim in drugim načinom, saj s pomočjo **hranjenja *logN* prednikov za vsako vozlišče** in uporabo teh za večje premike (skoke) po drvesu doseže še bolj optimalno časovno zahtevnost.  
Časovna zahtevnost predprocesiranja je **O(N logN)**, časovna zahevnost odgovarjanja na poizvedbe pa je **O(log N)**, kjer je N število vozlišč v drevesu. 

    Atributi:
    + **TreeNode root** &rarr; predstavlja korensko vozlišče drevesa, na katerem se bo izvajal algoritem za iskanje najnižjega skupnega prednika.
    + **TreeNode[][] ancestors** &rarr; predstavlja tabelo, v kateri za vsakega od vozlišč hranimo določeno število prednikov. **ancestors[i][j]** predstavlja 2^j - tega prednika za i-to vozlišče.
    + **int numNodes** &rarr; predstavlja število vozlišč v danem drevesu.
    + **int numAncestors** &rarr; predstavlja število prednikov, ki jih hranimo za vsako vozlišče. Izračuna se kot dvojiški logaritem višine danega drevesa.
    + **Map&lt;Integer, TreeNode&gt;** &rarr; predstavlja slovar, ki ima za ključe vrednosti vozlišč, vrednosti pa predstavljajo kazalce na vozlišča s temi vrednostmi. Ta slovar se uporablja, ker uporabnik v ukazno vrstico vnese vrednosti vozlišč, za kateri želi poiskati najnižjega skupnega prednika. Slovar pa potrebujemo zato, da za pripadajoče dobimo pripadajoča vozlišča, kar omogoča izvajanje algoritma.

    <br>

    Konstruktor:
    + **public LCA_binary_lift(TreeNode root)** &rarr; sprejme koren drevesa *root* in nastavi vrednosti atributom razreda, tako da s pomočjo pomožnih metod *getNumberOfNodes* in *height* iz razreda *UtilMethods* dobi število vozlišč in višino danega drevesa. Višina se uporabi, za izračun števila prednikov, ki jih bomo hranili za vsako vozlišče. Izračuna se kot dvojiški logaritem višine danega drevesa. Prav tako se incializira tabela prednikov *ancestors*. Konstruktor prav tako pokliče metodo *preprocess*, ki napolni tabelo prednikov *ancestors* in slovar *nodeMap*.

    <br>

    Metode:
    + **private void preprocess(TreeNode root)** &rarr; metoda izvede iskanje v globino (*depth first search*) in za vsako vozlišče shrani *numAncestors* prednikov. Za vsako vozlišče se shranijo predniki potence 2 nad posameznim vozliščem. Torej, če hranimo 3 prednike za vsako vozlišče, to pomeni, da hranimo prednike ki so 2^0, 2^1 in 2^2 nivojev nad tem vozliščem. Hkrati pa shranjuje pare vrednosti in pripadajočih vozlišč v slovar *nodeMap*.  

    + **public TreeNode getLCA(int node1_value, int node2_value)** &rarr; metoda sprejme vrednosti *node1_value* in *node2_value*, ki predstavljata vrednosti vozlišč, za kateri želimo poiskati najnižjega skupnega prednika. S pomočjo slovarja *nodeMap* pridobimo kazalce na vozlišči s podanimi vrednostmi. V primeru, da podanih vrednosti ni v slovarju, metoda vrne ustrezno napako.  
    Predpostavimo, da je vozlišče *node1* tisto, ki je globje, v nasprotnem primeru, pa vozlišči s pomočjo pomožne metode *swapNodes* zamenjamo.  Nato izračunamo razliko v globini med vozliščema in dokler vozlišči nista na istem nivoju, dvigujemo vozlišče, ki je globje - torej *node1*. To storimo tako, da razliko v globini zapišemo v dvojiškem zapisu in iz tega vzamemo  najvišji (najbolj levi) bit, ki je enak 1, in mesto, kjer je ta enica, predstavlja največji skok, ki ga lahko naredimo za vozlišče *node1*. Npr. razlika v viši za 2 vozlišči je 4 &rarr; dvojiško se to zapiše kot 0100 &rarr; najvišji bit, ki je enak 1 je bit na *drugem mestu* - to pomeni, da lahko z vozliščem *node1* "skočimo" na mesto prednika, ki  je 2^2 nivojev nad našim vozliščem.  
    Ko imamo vozlišči na enaki višini, pa se s pomočjo for zanke sprehodimo čez vse prednike obeh volišč. Če imata vozlišči različna prednika na določeni višini, "skočimo" na mesto teh prednikov. Po končani zanki se vozlišči nahajata točno en nivo pod njunim skupnim prednikom, tako da je najnižji skupni prednik kar neposreden starš kateregakoli od vozlišč, ki ga metoda tudi vrne.

---


  


