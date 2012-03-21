/* changer la classe d'un objet */
function SwitchClass(node,t) {
	node.className=t;
}

/* changer la classe de tous les objets d'une classe */
function SwitchClassForClass(theClass,t) {
	// recuperer tous les objets de cette classe
	 tab = getElementsByClass(theClass);
	for(var i=0;i<tab.length;i++){
		tab[i].className=t;
	}
}

/* changer la classe de tous les objets dont l'id contient thePrefix*/
function SwitchObjectsForClass(theId,t) {
	// recuperer tous les objets de cette classe
	tab = getDivsById(theId);
	//var liste = "";
	for(var i=0;i<tab.length;i++){
		/* if (theId == 'Jm') {
			alert("tab[i]="+tab[i]+ " class="+t);
		}*/
		// avec mozilla, certain elements sans classe sont ramenes
		if (tab[i].className != "") {
			tab[i].className=t;
			//liste = liste + tab[i].className + "\n";
		}
	}
	//alert("theId="+theId+" t="+t+"\n\n" + liste);
}

/* Retourne tout les elements du documents ayant comme class maClass */
function getElementsByClass(maClass) {
	/*    Tableau comportant les elements de retour*/
    var tabRetour = new Array();
    /* Tableau temporaire necessaire pour faire la boucle et rechercher
         les elements ayant la classe precise, il va contenir tout les
         elements du documents */
    var tabTmp = new Array();
    /* Recupere tout les elements du document */
    tabTmp = document.getElementsByTagName("*");
    /* Compteur, sert a incrementer le tableau des valeurs de retour */
    j=0;
    /* Pour chaque element de tabTmp */
    for (i=0; i<tabTmp.length; i++) {
        /* on regarde si la classe de l'i-eme element est celle qu'on recherche */
        if (tabTmp[i].className==maClass) {
            /*si oui on ajoute dans notre tableau de retour a l'index j */
            tabRetour[j]=tabTmp[i];
            /* on incremente de 1 j pour pas reecrire l'element suivant par dessus */
            j++;
        }
     /* Element suivant ... */
    }
     /* on retourne notre tableau d'elements ayant la classe specifie*/
	 //alert('total time : ' + (debut-fin));
    return tabRetour;
 } 

 /* Retourne tout les elements du documents dont l'id commence par myPrefix*/
function getDivsById(myId) {
	var tabRetour = new Array();
    var tabTmp = new Array();
    tabTmp = document.getElementsByTagName("div");
    j=0;
    for (i=0; i<tabTmp.length; i++) {
		var anId = tabTmp[i].id +'';
	    if (anId.lastIndexOf(myId, anId.length) > 0) {
            tabRetour[j]=tabTmp[i];
            j++;
        }
    }
  	return tabRetour;
 } 
