class DateSearch {

    static months = ['leden', 'únor', 'březen', 'duben', 'květen', 'červen', 'červenec','srpen', 'záři', 'říjen', 'listopad', 'prosinec']

    startChosen = false
    endChosen   = false
    startDate   = new Date()
    endDate     = new Date()

    constructor(submitE, startE, endE) {
        this.submitE = submitE
        this.startE  = startE
        this.endE    = endE
        this.init()
    }

    init(){
        
        this.submitE.addEventListener('click', (e) => {
            e.preventDefault();
            let tmp = window.location.href;
            let url = ( tmp.indexOf('?') == -1 ) ? 
                tmp + '/?page=1':
                tmp.substr(0, tmp.indexOf('?')) + '/?page=1';
            if (this.startChosen)
                url += '&from=' + this.startDate.toISOString().substring(0, 10); 
            if (this.endChosen)
                url += '&until=' + this.endDate.toISOString().substring(0, 10);
            window.location.href = url;
            toggleHidden(['toggle-date-start','toggle-date-end'],['date-start','date-end','date-search-submit'])
        });

        this.startE.addEventListener('click', (e) => {
            if ( 
                !(e.target.firstChild.nodeValue >= 1 && e.target.firstChild.nodeValue <= 31 )
            )
                return;
            e.preventDefault()
            let day = parseInt(e.target.firstChild.nodeValue) + 1;
            console.log(day)
            let monthstr = this.startE.querySelector('#month').firstChild.nodeValue;
            let month;
            DateSearch.months.forEach((m,i) => {
                if ( m == monthstr )
                    month = i;
            });
            let year = this.startE.querySelector('#year').firstChild.nodeValue;
            if ( e.target.classList[0] == 'diff-month' ){
                if ( day >= 21 ){
                    month -= 1;
                    if ( month == -1 ){
                        year -= 1;
                        month = 11;
                    }
                }
                else if ( day <= 7 ){
                    month += 1;
                    if ( month == 12 ){
                        year += 1;
                        month = 0;
                    }
                }
            }
            this.startDate = new Date( year, month, day );
            this.startChosen = true;
        });


        this.endE.addEventListener('click', (e) => {
            if ( 
                !(e.target.firstChild.nodeValue >= 1 && e.target.firstChild.nodeValue <= 31 )
            )
                return;
            e.preventDefault()
            let day = parseInt(e.target.firstChild.nodeValue) + 1;
            let monthstr =  this.endE.querySelector('#month').firstChild.nodeValue;
            let month;
            DateSearch.months.forEach((m,i) => {
                if ( m == monthstr )
                    month = i;
            });
            let year = this.endE.querySelector('#year').firstChild.nodeValue;
            if ( e.target.classList[0] == 'diff-month' ){
                if ( day >= 21 ){
                    month -= 1;
                    if ( month == -1 ){
                        year -= 1;
                        month = 11;
                    }
                }
                else if ( day <= 7 ){
                    month += 1;
                    if ( month == 12 ){
                        year += 1;
                        month = 0;
                    }
                }
            }
            this.endDate = new Date( year, month, day );
            this.endChosen = true;
        });
    }
}

const searchDateSubmit = document.getElementById('date-search-submit')
const searchDateStart = document.getElementById('date-start')
const searchDateEnd = document.getElementById('date-end')
new DateSearch(searchDateSubmit,searchDateStart,searchDateEnd);