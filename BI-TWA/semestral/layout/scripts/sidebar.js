function winSize(){
    const currentW = window.innerWidth
    return currentW > 991 ? 
            'large' : currentW > 600 ? 
                      'middle' : 
                      'small'
}

class Sidebar{
    constructor(pageE, sidebarE, controllerE){
        this.pageE = pageE
        this.sidebarE = sidebarE
        this.controllerE = controllerE

        /*------- EVENT LISTENERS --------*/
        this.controllerE.addEventListener('click', this.toggle.bind(this))
        this.controllerE.addEventListener('click', ()=>{
            this.pageE.classList.add('transition-margin-left')
            this.sidebarE.classList.add('transition-width')
            this.sidebarE.classList.add('transition-opacity')
        }, {once: true})

        this.sidebarE.classList.add('sidebar-compact-show')
        this.pageE.classList.add('page-sidebar-open', 'animate-margin')

        const savedStateLarge = localStorage.getItem('sidebar-state-large');
    
        console.log(savedStateLarge)
        this.open = {large : (savedStateLarge === 'open') ? true : false, 
                     middle : false, 
                     small : false}

        this.currentSize = winSize()
        this.proceedChange()

        window.addEventListener('resize', (e) => {
            this.changeSize()

        }, true);
    }

    proceedChange(){
        console.log(this.currentSize, this.open)
        if(this.open[this.currentSize]){
            this.pageE.classList.add('page-sidebar-open')
            this.pageE.classList.remove('page-sidebar-collapsed')
            this.sidebarE.classList.remove('sidebar-compact-hide')
            this.sidebarE.classList.add('sidebar-compact-show')
            this.controllerE.classList.remove('collapsed')
        }else{
            this.pageE.classList.remove('page-sidebar-open')
            this.pageE.classList.add('page-sidebar-collapsed')
            this.sidebarE.classList.add('sidebar-compact-hide')
            this.sidebarE.classList.remove('sidebar-compact-show')
            this.controllerE.classList.add('collapsed')
        }
    }

    changeSize(){
        const newSize = winSize()
        if(this.currentSize === newSize){ return }

        this.currentSize = newSize
        
        this.proceedChange()
    }

    toggle(){
        console.log('toggle')

        this.open[this.currentSize] = !this.open[this.currentSize]
        localStorage.setItem('sidebar-state-large', (this.open.large) ? 'open' : 'close');

        
        this.pageE.classList.toggle('page-sidebar-open')
        this.pageE.classList.toggle('page-sidebar-collapsed')
        this.sidebarE.classList.toggle('sidebar-compact-hide')
        this.sidebarE.classList.toggle('sidebar-compact-show')
        this.controllerE.classList.toggle('collapsed')
    }
}



const sidebarElem = document.getElementById('sidebar')
const constrollerElem = document.getElementById('toggle-sidebar')
const pageElem = document.getElementById('page-container')
const sideBar = new Sidebar(pageElem, sidebarElem, constrollerElem)
