document.onkeydown = function () {
    if (event.keyCode == 13) {
        if (event.srcElement.tagName != 'BUTTON')
            event.keyCode = 9;
        else
            event.srcElement.click();
    }
}

function getMaxDateYM() {
    var txndate1 = document.getElementById("inputform:txndate1").value;
    if (txndate1 != '') {
        txndate1 = txndate1.substr(0, 4) + '-12';
    } else {
        txndate1 = '';
    }
    return txndate1;
}

function getMinDateYM() {
    var txndate2 = document.getElementById("inputform:txndate2").value;
    if (txndate2 != '') {
        txndate2 = txndate2.substr(0, 4) + '-01';
    } else {
        txndate2 = '';
    }
    return txndate2;
}

function affirmOper() {
    if (confirm("确定?"))
        return true;
    else
        return false;
}

function affirmReInPage() {
    if (confirm("是否已重新进纸?"))
        return true;
    else
        return false;
}

