//TODO: remember active week & improve code (diff components / state)

class Calendar{
    static months = ['leden', 'únor', 'březen', 'duben', 'květen', 'červen', 'červenec','srpen', 'záři', 'říjen', 'listopad', 'prosinec']
    static days = ['po', 'út', 'st', 'čt', 'pá', 'so', 'ne']

    constructor(
        calendarControl, 
        calendarData,
        monthElem,
        yearElem
    )
    {
        /*-------- COMPONENTS --------*/
        this.control = calendarControl
        this.calendarData = calendarData
        this.monthElem = monthElem
        this.yearElem = yearElem

        /*-------- STATE --------*/
        this.currDate = new Date()
        this.activeDate
        this.currDate.setDate(1)
        this.daysArr = []
        this.activeDayElem = null
        
        /*-------- INIT DAYS ARR --------*/
        this.updateDaysArr()
    }

    updateDaysArr() {
        this.daysArr = []

        const daysInPrevMonth = this.daysInPrevMonth()
        for(let i = this.currDate.getDay() - 1; i > 0; --i){
            this.daysArr.push({val : daysInPrevMonth - i, type: 0})
        }

        for (let i = 1; i <= this.daysInCurrMonth(); ++i) {
            this.daysArr.push({val : i, type: 1})           
        }
        
        for(let i = 1; i <= this.currDate.getDay() + 1; ++i){
            let j = 1
            for (; i < 35; ++i) {
                this.daysArr.push({val : j++, type: 0})            
            }
        }
    }

    daysInCurrMonth() {
        return new Date(this.currDate.getFullYear(), this.currDate.getMonth()+1, 0).getDate();
    }

    daysInPrevMonth() {
        return new Date(this.currDate.getFullYear(), this.currDate.getMonth(), 0).getDate();
    }

    next(){
        this.currDate = new Date(this.currDate.getFullYear(), this.currDate.getMonth() + 1);
        this.updateDaysArr();
        this.redraw();
    }

    prev(){
        this.currDate.setDate(0);
        this.currDate.setDate(1);
        this.updateDaysArr();
        this.redraw();
    }

    redraw(){
        this.monthElem.textContent = Calendar.months[this.currDate.getMonth()];
        this.yearElem.textContent = this.currDate.getFullYear();

        this.calendarData.innerHTML = '';
        for(let i = 0; i < 5; ++i){
            const tr = document.createElement('tr');
            for (let j = 0; j < 7; j++) {
                const td = document.createElement('td');
                const day = this.daysArr[i * 7 + j];
                td.textContent = day["val"];
                if(day.type == 0){
                    td.classList.add("diff-month");
                }
                tr.append(td);
                td.addEventListener('click',()=>{
                    if(this.activeDayElem){this.activeDayElem.classList.toggle('active-day');}
                    td.classList.toggle('active-day');
                    this.activeDayElem = td;
                    if ( td.classList[0] != 'diff-month' )
                        this.activeDate = new Date( this.currDate.getFullYear(), this.currDate.getMonth(), td.textContent );
                    else if ( td.textContent >= 21 ){
                        if ( this.currDate.getMonth() == 0 )
                            this.activeDate = new Date( this.currDate.getFullYear()-1, 11, td.textContent );
                        else
                            this.activeDate = new Date( this.currDate.getFullYear(), this.currDate.getMonth()-1, td.textContent );
                    }
                    else if ( td.textContent <= 7 ){
                        if ( this.currDate.getMonth() == 11 )
                            this.activeDate = new Date( this.currDate.getFullYear()+1, 0, td.textContent );
                        else
                            this.activeDate = new Date( this.currDate.getFullYear(), this.currDate.getMonth()+1, td.textContent );
                    }
                })
                if (
                    this.activeDate &&
                    this.activeDate.getFullYear() == this.currDate.getFullYear() &&
                    this.activeDate.getMonth() == this.currDate.getMonth() &&
                    this.activeDate.getDate() == td.textContent &&
                    td.classList[0] != 'diff-month'
                ){
                    td.classList.toggle('active-day');
                    this.activeDayElem = td;
                }
                else if ( td.classList[0] == 'diff-month' ){
                    if ( td.textContent <= 7 ){
                        if ( 
                            this.currDate.getMonth() == 11 &&
                            this.activeDate &&
                            this.activeDate.getFullYear() == this.currDate.getFullYear() + 1 &&
                            this.activeDate.getMonth() == 0 &&
                            this.activeDate.getDate() == td.textContent
                        ){
                            td.classList.toggle('active-day');
                            this.activeDayElem = td;
                        }
                        else if ( 
                            this.currDate.getMonth() != 11 &&
                            this.activeDate &&
                            this.activeDate.getFullYear() == this.currDate.getFullYear() &&
                            this.activeDate.getMonth() == this.currDate.getMonth() + 1 &&
                            this.activeDate.getDate() == td.textContent
                        ){
                            td.classList.toggle('active-day');
                            this.activeDayElem = td;
                        }
                    }
                    else if ( td.textContent >= 21 ){
                        if ( 
                            this.currDate.getMonth() == 0 &&
                            this.activeDate &&
                            this.activeDate.getFullYear() == this.currDate.getFullYear() - 1 &&
                            this.activeDate.getMonth() == 11 &&
                            this.activeDate.getDate() == td.textContent
                        ){
                            td.classList.toggle('active-day');
                            this.activeDayElem = td;
                        }
                        else if ( 
                            this.currDate.getMonth() != 0 &&
                            this.activeDate &&
                            this.activeDate.getFullYear() == this.currDate.getFullYear() &&
                            this.activeDate.getMonth() == this.currDate.getMonth() - 1 &&
                            this.activeDate.getDate() == td.textContent
                        ){
                            td.classList.toggle('active-day');
                            this.activeDayElem = td;
                        }
                    }
                }
            }
            this.calendarData.append(tr);
        }
    }
}

document.querySelectorAll(".calendar-wrapper").forEach(e => {
    const calendarData = e.querySelector('#calendar-data');
    const calendarControl = e.querySelector('#calendar-control');
    const monthElem = e.querySelector('#month');
    const yearElem = e.querySelector('#year');
    
    const calendar = new Calendar(calendarControl, calendarData, monthElem, yearElem);
    
    const prevController = e.querySelector('#prev-month');
    const nextController = e.querySelector('#next-month');
    prevController.addEventListener('click', calendar.prev.bind(calendar));
    nextController.addEventListener('click', calendar.next.bind(calendar));
    
    calendar.redraw();
});




function toggleHidden(show,hide){
    show.forEach( id => {
        document.getElementById(id).style.display = "block";
    });
    hide.forEach( id => {
        document.getElementById(id).style.display = "none";
    });
}