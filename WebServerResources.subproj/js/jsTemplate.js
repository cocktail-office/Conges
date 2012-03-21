if(document.all)
{
   navigator.family = "ie4";
}
if(window.navigator.userAgent.toLowerCase().match(/gecko/))
{
   navigator.family = "gecko";
}
if(window.navigator.userAgent.toLowerCase().indexOf('opera') != - 1)
{
   navigator.family = 'opera';
}
var myWindow;

   /**
   ouvrir une popup sur le planning (saisie d'occupation, saisie d'horaire, detail d'occupation)
       */
   function ouvrirPopup(href, height) {
	window.name='pageprincipale';
	window.open(href,'','status=no,top=200,left=400,width=400,height='+height+',rezise=no,menubar=no,toolbar=no,location=no');
	return false;
   }

function openCenteredWindow(url)
{
   var width = 400;
   var height = 300;
   var left = parseInt((screen.availWidth /2) - (width/ 2));
   var top = parseInt((screen.availHeight /2) - (height/ 2));
   var windowFeatures = "width=" + width + ",height=" + height + ",status,resizable,left=" + left + ",top=" + top + ",screenX=" + left + ",screenY=" + top;
   myWindow = window.open(url, "subWind", windowFeatures);
}

function cngFormatter(tf)
{
   var tfValue = tf.value;
   var length = tfValue.length;
   var newTfValue = "";
   // Suppression des caracteres indesirables
   for(var i = 0; i < length; i++)
   {
      var unChar = tfValue.charAt(i);
      if(":0123456789".indexOf(unChar) >= 0)
      {
         newTfValue += unChar;
      }
   }
   tfValue = newTfValue;
   tf.value = tfValue;
   length = tfValue.length;
   if(length > 0)
   {
      var index = tfValue.indexOf(":");
      var heures = "";
      var minutes = "";
      if(index == - 1)
      {
         if(length <= 2)
         {
            heures = tfValue.substr(0, length);
            minutes = "0";
         }
         else if(length == 3)
         {
            heures = tfValue.substr(0, 1);
            minutes = tfValue.substr(1, 2);
         }
         else 
         {
            heures = tfValue.substr(0, 2);
            minutes = tfValue.substr(2, 2);
         }
      }
      else 
      {
         if(length > 1)
         {
            heures = tfValue.substr(0, index);
            if(length - 1 == index)
            {
               minutes = "00";
            }
            else 
            {
               minutes = tfValue.substr(index + 1, length - index);
            }
         }
         else 
         {
            alert('Horaire invalide');
         }
      }
      if((parseInt(heures) > 23) ||(parseInt(minutes) > 59))
      {
         alert('Horaire invalide');
      }
      else 
      {
         if(heures.length < 2 && parseInt(heures) < 10)
         {
            heures = "0".concat(parseInt(heures));
         }
         if(minutes.length < 2 && parseInt(minutes) < 10)
         {
            minutes = minutes + "0";
         }
         tf.value = heures + ':' + minutes;
      }
   }
}

function validerHoraire(
		tf, dureeMiniPMeridienne, minDebPMeridienne, maxFinPMeridienne, minDebAM, maxDebAM, 
		minFinPM, maxFinPM, dureePRTT, dureeMini, demiJourneeDureeMaxi, passeDroit) {

	// test si le format est OK ou 		
	if(cngFormatter(tf) == false);
	
	var tfId = tf.id;
  var lengthId = tfId.length;
  var jour = tfId.substr(tfId.indexOf('_') + 1, lengthId);
  var debutAMId = "debutAM_" + jour;
  var debutAM = document.getElementById(debutAMId);
  var valueDebutAM = debutAM.value;
  var finAMId = "finAM_" + jour;
  var finAM = document.getElementById(finAMId);
  var valueFinAM = finAM.value;
  var debutPMId = "debutPM_" + jour;
  var debutPM = document.getElementById(debutPMId);
  var valueDebutPM = debutPM.value;
  var finPMId = "finPM_" + jour;
  var finPM = document.getElementById(finPMId);
  var valueFinPM = finPM.value;
  var totalJournalierId = "totalJournalier_" + jour;
  var totalJournalier = document.getElementById(totalJournalierId);
  var debutPauseId = "debutPause_" + jour;
  var debutPause = document.getElementById(debutPauseId);
  var valueDebutPause = debutPause.value;
  var duree = 0;
  var error = "";
  var nextFocus = "";
   
  // controle coherence horaire matin
  if(error == "" && valueDebutAM != "" && valueFinAM != "") {
   	if(to_minutes(valueFinAM) <= to_minutes(valueDebutAM)) {
    	error = "Matin : l'heure d'embauche doit etre anterieure a l'heure de debauche";
		} else {
    	duree +=(to_minutes(valueFinAM) - to_minutes(valueDebutAM));
   	}
  }
   
  // controle coherence horaire apres midi
  if(error == "" && valueDebutPM != "" && valueFinPM != "") {
  	if(to_minutes(valueFinPM) <= to_minutes(valueDebutPM)) {
			error = "Apres-midi : l'heure d'embauche doit etre anterieure a l'heure de debauche";
		} else {
    	duree +=(to_minutes(valueFinPM) - to_minutes(valueDebutPM));
   	}
	}
	
	// controle début matin minimum
  if (minDebAM > 0) {
  	if (valueDebutAM != "" && to_minutes(valueDebutAM) < minDebAM) {
    	error = "Vous ne pouvez pas commencer la journee avant " + to_horaire(minDebAM);
  	}
 	}
   
	// controle début matin maximum
  if (error == "" && maxDebAM > 0) {
  	if (valueDebutAM != "" && to_minutes(valueDebutAM) > maxDebAM) {
   		error = "Vous ne pouvez pas commencer la journee apres " + to_horaire(maxDebAM);
  	}
	}
   
	// controle fin matin minimum
  if (error == "" && minDebPMeridienne > 0) {
  	if (valueFinAM != "" && to_minutes(valueFinAM) < minDebPMeridienne) {
  		error = "Vous ne pouvez pas finir la matinee avant " + to_horaire(minDebPMeridienne) + ".";
  	}
 	}
   
	// controle debut apres midi maximum
  if (error == "" && valueDebutPM != "" && maxFinPMeridienne > 0) {
  	if (to_minutes(valueDebutPM) > maxFinPMeridienne) {
    	error = "Vous ne pouvez pas reprendre apres " + to_horaire(maxFinPMeridienne);
  	}
 	}
  
  // controle debauche matin avant embauche apres midi 
  if (error == "" && valueFinAM != "" && valueDebutPM != "") {
  	if (to_minutes(valueFinAM) >= to_minutes(valueDebutPM)) {
    	error = "L'heure de debauche du matin doit etre anterieure a la date d'embauche de l'apres midi";
  	} 
  }
   
	// controle debauche apres midi mini
  if (error == "" && valueFinPM != "" && minFinPM > 0) {
  	if(to_minutes(valueFinPM) < minFinPM) {
    	error = "Vous ne pouvez pas finir avant " + to_horaire(minFinPM);
    }
  }
   
	// controle debauche apres midi maxi
  if (error == "" && valueFinPM != "" && maxFinPM > 0) {
  	if(to_minutes(valueFinPM) > maxFinPM) {
    	error = "Vous ne pouvez pas finir apres " + to_horaire(maxFinPM);
  	}
  }

	// positionnement automatique de la pause RTT en début de pause méridienne 
  if(error == "" && valueFinAM != "" && valueDebutPM != "") {
  	if(tf != debutPause) {
    	if(duree >= dureeMini) {
      	debutPause.disabled = "";
        debutPause.className = "TFCenter";
        debutPause.select();
        if (valueFinAM != "") {
        	debutPause.value = to_horaire(to_minutes(valueFinAM) - dureePRTT);
        	//if (valueDebutPause == "") {
        		valueDebutPause = debutPause.value;
        	//}
        	debutPause.form.submit();
        	debutPause.focus();
        }
      } else {
      	debutPause.value = "";
        debutPause.disabled = "true";
        debutPause.className = "TFCenterGrey";
      }
    }
  }

  // controle duree pause meridienne
  if (error == "" && valueDebutAM != "" && valueFinAM != "" && valueDebutPM != "" && valueFinPM != "") {
  	if (dureeMiniPMeridienne > 0) {
    	// est-ce que la pause RTT est collée à la pause méridienne 
      debutPauseMinutes = to_minutes(valueDebutPause);
      isPauseRTTCollee = ((to_minutes(valueFinAM) - dureePRTT == debutPauseMinutes) || (to_minutes(valueFinPM) == debutPauseMinutes));
      // controle que la duree "entre midi et 2" est pas en dessous a la limite
      dureeTotalePauseMeridienne = to_minutes(valueDebutPM) - to_minutes(valueFinAM);
      if (isPauseRTTCollee) {
      	dureeTotalePauseMeridienne += dureePRTT;
      }
      isPauseMeridienneTooShort = (dureeTotalePauseMeridienne < dureeMiniPMeridienne + dureePRTT);
    	// aie aie
    	if (isPauseMeridienneTooShort) {
    		error = "La duree totale de la pause meridienne doit-etre de " + (dureeMiniPMeridienne + dureePRTT) + "min au minimum\n\n" +
    	     "Ici, la duree totale de la pause est de " + dureeTotalePauseMeridienne + " min";
      }
   	}
  }

	// NE CONCERNE PAS LES PASSE DROIT
  // gestion pause meridienne obligatoire ou pas (et controle des heures de fin AM et debut AM)
	if(error == "" && passeDroit == "N") {
  	
  	// le test est différent selon si l'agent travaille
  	// les 2 demi journées ou non
  	isTravail2DemiJournees = false;
  	if (valueDebutAM != "" && valueFinAM != "" && valueDebutPM != "" && valueFinPM != "") {
  		isTravail2DemiJournees = true;
  	}
  	
    // fin max de la matinee
  	if (isTravail2DemiJournees) {

	  	var debutPauseMinutes = to_minutes(valueFinAM) - dureePRTT;
	   	var maxFinAM = maxFinPMeridienne - dureeMiniPMeridienne; // 13h35
	   	if (debutPauseMinutes != (maxFinAM - dureePRTT)) {
	   		maxFinAM = maxFinAM - dureePRTT; // 13h15
			}
   	
	   	var valueFinAMMinutes = to_minutes(valueFinAM);
	   	if (valueFinAMMinutes > maxFinAM) {
		   	error = "Travail sur les deux demi-journees\n\nVous devez prendre la pause meridienne avant " + to_horaire(maxFinAM);
		   	
	   	}
   		
  	} else {
  	
	  	var debutPauseMinutes = to_minutes(valueFinAM) - dureePRTT;
	   	var maxFinAM = maxFinPMeridienne; // 14h00
	   	if (debutPauseMinutes != (maxFinAM - dureePRTT)) {
	   		maxFinAM = maxFinAM - dureePRTT; // 13h40
			}
   	
	   	var valueFinAMMinutes = to_minutes(valueFinAM);
	   	if (valueFinAMMinutes > maxFinAM) {
		   	error = "Travail sur la seule demi-journee du matin\n\nVous devez prendre la pause meridienne avant " + to_horaire(maxFinAM);
	   	}
  	
  	}
  	
   	// debut min de l'aprem
   	if (error == "") {

	    var minDebutPM = minDebPMeridienne + dureeMiniPMeridienne; // 11h45 - 0h25 = 12h10
	   	if (debutPauseMinutes != minDebPMeridienne - dureePRTT) {
	   		minDebutPM = minDebutPM + dureePRTT; // 12h30
			}
			if (valueDebutPM != "") {
	   		var valueDebutPMMinutes = to_minutes(valueDebutPM);
	   		if (valueDebutPMMinutes < minDebutPM) {
		    	error = "Vous ne pouvez pas rembaucher avant " + to_horaire(minDebutPM);
		   	}
			}
   	
   	}
	}
	
	// NE CONCERNE PAS LES PASSE DROIT
	// controle de la durée maximum des demi journées AM puis PM
	if (error == "" && passeDroit == "N") {
		// le matin ...
		if (valueDebutAM != "" && valueFinAM != "") {
			if (to_minutes(valueFinAM) - to_minutes(valueDebutAM) > demiJourneeDureeMaxi) {
				error = "Matin : la duree de la demi-journee ne peut exceder " + to_horaire(demiJourneeDureeMaxi) + " consecutives (pause inclue)";
			}
		}
		// l'après midi
		if (error == "" && valueDebutPM != "" && valueFinPM != "") {
			if (to_minutes(valueFinPM) - to_minutes(valueDebutPM) > demiJourneeDureeMaxi) {
				error = "Apres-midi : la duree de la demi-journee ne peut exceder " + to_horaire(demiJourneeDureeMaxi) + " consecutives (pause inclue)";
			}
		}
	}

	if(error == "" && valueDebutPause != "" && valueDebutAM != "" && valueFinPM != "") {
  	var debutPauseMinutes = to_minutes(valueDebutPause);
    if(debutPauseMinutes == to_minutes(valueDebutAM) || debutPauseMinutes + dureePRTT == to_minutes(valueFinPM)) {
    	error = "La pause RTT ne peut pas etre prise en debut ou en fin de journee.";
    }
    if(	
      		debutPauseMinutes <= to_minutes(valueDebutAM) ||
      		(
      			debutPauseMinutes <= to_minutes(valueFinAM) && debutPauseMinutes > to_minutes(valueFinAM) - dureePRTT
      		) ||
      		(
      			debutPauseMinutes < to_minutes(valueDebutPM) && debutPauseMinutes > to_minutes(valueFinAM)
      		) ||
      		(
      			debutPauseMinutes <= to_minutes(valueFinPM) && debutPauseMinutes >= to_minutes(valueFinPM) - dureePRTT
      		) || 
      		debutPauseMinutes > to_minutes(valueFinPM)) {
      error = "Horaire du debut de la pause RTT incorrect.";
	    debutPause.select();
			debutPause.focus();
   	}
  }


  totalJournalier.value = to_duree(duree);
  if(error != "") {
  	alert(error);
  } else {
  	if(((tf == debutAM) &&(valueFinAM != "")) ||
      ((tf == debutPM) &&(valueFinPM != "")) ||
      ((tf == finAM) &&(valueDebutAM != "")) ||
      ((tf == finPM) &&(valueDebutPM != "")) ||
      (((tf == debutAM) ||(tf == finAM)) &&(valueDebutAM == "") &&(valueFinAM == "")) ||
      (((tf == debutPM) ||(tf == finPM)) &&(valueDebutPM == "") &&(valueFinPM == "")) ||
      ((tf == debutPause))) {
     	// determiner ou se fera le prochain focus
     	//if (
     	 	
      	tf.form.submit();
      }
   }   
}

//-------------------------------------------------------------------
// isDigit(value)
//   Returns true if value is a 1-character digit
//-------------------------------------------------------------------
function isDigit(num) {
	var numStr = ""+num;
	if (numStr == "" || numStr.length>1) {
		return false;
	}
	var strDigit = "1234567890";
	if (strDigit.indexOf(numStr)!=-1) {
		return true;
	}
	return false;
}
	
// ----------------------------------------------------------------------------	
// conversion d'un horaire en sa valeur minutes
// ----------------------------------------------------------------------------
function to_minutes(horaire) {
	var horaireAbs = horaire;
	var to_minutes = 0;
	
	// determiner le signe 
	var isNegatif = false;
	if (horaireAbs.indexOf("-") == 0) {
		isNegatif = true;
		horaireAbs = horaireAbs.substr(1, horaireAbs.length);
	}

	// trouver la valeur des heures
	var index = 0;
	
	while (isDigit(horaireAbs.charAt(index)) && index < horaireAbs.length) {
		index++;
	}

	if (index  >= horaireAbs.length) {
		return 0;
	}
	// ignorer les 0 des heures
	var heures = horaireAbs.substr(0, index);
	if (heures.indexOf("0") == 0) {
		while (heures.indexOf("0") == 0 && heures.length > 0) {	
			heures = heures.substr(1,heures.length);
		}
		if (heures == "") {
			heures = "0";
		}
	}
	
	var minutes = horaireAbs.substr(index+1, 2);
	to_minutes = parseInt(heures) * 60 + parseInt(minutes);
	// remettre le signe
	if (isNegatif) {
		to_minutes = -to_minutes;
	}
	
	return to_minutes;
}

// --------------------------------------------------------------------------------------------------------
// convertirHeuresToJoursFromElement(string, inputText, spanOuDiv)
//   valHeures : la valeur numerique des heures qui seront converties
//   tfDuree : l'element HTML input de type "text" contenant la base de conversion 
//   idEltJours : le nom de l'element HTML (div ou span) contenant le resultat de la conversion
// --------------------------------------------------------------------------------------------------------
function convertirHeuresToJoursFromElement(valHeures, inputDuree, idEltJours) {
	var valMinutes 	= to_minutes(valHeures);
	var valDuree	= to_minutes(inputDuree.value);
	// on coupe 2 chiffres apres la virgule
	var valResultat = (valMinutes/valDuree).toFixed(2);
	document.getElementById(idEltJours).innerHTML = valResultat;
}

function to_horaire(duree)
{
   var to_horaire = "";
   var heures = Math.floor(duree/60);
   var minutes = duree -(heures * 60);
   if(heures < 10) to_horaire = "0";
   to_horaire += heures + ":";
   if(minutes < 10) to_horaire += "0";
   to_horaire += minutes;
   return to_horaire;
}

function to_duree(duree)
{
   var to_duree = "";
   var heures = Math.floor(duree/60);
   var minutes =(duree/1) -(heures * 60);
   if(heures < 10) to_duree = "0";
   to_duree += heures + "h";
   if(minutes < 10) to_duree += "0";
   to_duree += minutes;
   return to_duree;
}

function copierHorairePrecedent(source, dest)
{
   var sourceDebutAMId = "debutAM_" + source;
   var sourceDebutAM = document.getElementById(sourceDebutAMId);
   var sourceValueDebutAM = sourceDebutAM.value;
   var sourceFinAMId = "finAM_" + source;
   var sourceFinAM = document.getElementById(sourceFinAMId);
   var sourceValueFinAM = sourceFinAM.value;
   var sourceDebutPMId = "debutPM_" + source;
   var sourceDebutPM = document.getElementById(sourceDebutPMId);
   var sourceValueDebutPM = sourceDebutPM.value;
   var sourceFinPMId = "finPM_" + source;
   var sourceFinPM = document.getElementById(sourceFinPMId);
   var sourceValueFinPM = sourceFinPM.value;
   var sourceTotalJournalierId = "totalJournalier_" + source;
   var sourceTotalJournalier = document.getElementById(sourceTotalJournalierId);
   var sourceValueTotalJournalier = sourceTotalJournalier.value;
   var sourceDebutPauseId = "debutPause_" + source;
   var sourceDebutPause = document.getElementById(sourceDebutPauseId);
   var sourceValuedebutPause = sourceDebutPause.value;
   var debutAMId = "debutAM_" + dest;
   var debutAM = document.getElementById(debutAMId);
   var finAMId = "finAM_" + dest;
   var finAM = document.getElementById(finAMId);
   var debutPMId = "debutPM_" + dest;
   var debutPM = document.getElementById(debutPMId);
   var finPMId = "finPM_" + dest;
   var finPM = document.getElementById(finPMId);
   var totalJournalierId = "totalJournalier_" + dest;
   var totalJournalier = document.getElementById(totalJournalierId);
   var debutPauseId = "debutPause_" + dest;
   var debutPause = document.getElementById(debutPauseId);
   var horaireTotalId = "HoraireHebdomadaireTotal";
   var horaireTotal = document.getElementById(horaireTotalId);
   debutAM.value = sourceValueDebutAM;
   finAM.value = sourceValueFinAM;
   debutPM.value = sourceValueDebutPM;
   finPM.value = sourceValueFinPM;
   totalJournalier.value = sourceValueTotalJournalier;
   debutPause.value = sourceValuedebutPause;
   debutPause.disabled = sourceDebutPause.disabled;
   var duree = 0;
   if(horaireTotal.value != "") {
      duree += to_minutes(horaireTotal.value);
   }
   duree += to_minutes(totalJournalier.value);
   horaireTotal.value = to_duree(duree);
}

function effacerHorairePrecedent(jour)
{
   var debutAMId = "debutAM_" + jour;
   var debutAM = document.getElementById(debutAMId);
   var finAMId = "finAM_" + jour;
   var finAM = document.getElementById(finAMId);
   var debutPMId = "debutPM_" + jour;
   var debutPM = document.getElementById(debutPMId);
   var finPMId = "finPM_" + jour;
   var finPM = document.getElementById(finPMId);
   var debutPauseId = "debutPause_" + jour;
   var debutPause = document.getElementById(debutPauseId);
   debutAM.value = "";
   finAM.value = "";
   debutPM.value = "";
   finPM.value = "";
   debutPause.value = "";
   debutPause.disabled = "true";
   return false;
}


function cngFormatterDuree(tf)
{
   var tfValue = tf.value;
   var length = tfValue.length;
   var newTfValue = "";
   
   
   // Suppression des caracteres indesirables
   for(var i = 0; i < length; i++)
   {
      var unChar = tfValue.charAt(i);
      if(":0123456789".indexOf(unChar) >= 0)
      {
         newTfValue += unChar;
      }
   }
   tfValue = newTfValue;
   tf.value = tfValue;
   length = tfValue.length;
   
   if(length > 0)
   { 
      var index = tfValue.indexOf(":");
      var heures = "";
      var minutes = "";
      if(index == - 1)
      {
         if(length <= 2)
         {
            heures = tfValue.substr(0, length);
            minutes = "0";
         }
         else 
         {
            if(length == 3)
            {
               heures = tfValue.substr(0, 1);
               minutes = tfValue.substr(1, 1);
            }
            else 
            {
               heures = tfValue.substr(0, 2);
               minutes = tfValue.substr(2, 1);
            }
         }
      }
      else 
      {
         if(length > 1)
         {
            heures = tfValue.substr(0, index);
            if(length - 1 == index)
            {
               minutes = "00";
            }
            else 
            {
               minutes = tfValue.substr(index + 1, length - index);
            }
         }
         else 
         {
            alert('Duree invalide');
         }
      }
      if(parseInt(minutes) > 59)
      {
         alert('Duree invalide');
      }
      else 
      {
         if(heures.length < 2 && parseInt(heures) < 10)
         {
            heures = "0".concat(parseInt(heures));
         }
         if(minutes.length < 2 && parseInt(minutes) < 10)
         {
            minutes = minutes + "0";
         }
         tf.value = heures + ':' + minutes;
      }
   }
   

}