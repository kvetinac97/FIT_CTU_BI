class SubMenu{
    constructor(menu, controller) {
        this.menu = menu
        this.controller = controller
        this.registerToggler(controller)
    }

    open(){
        this.menu.classList.remove('hide-m')
    }

    close(){
        this.menu.classList.add('hide-m')
    }

    toggle(){
        this.menu.classList.toggle('hide-m')
    }

    registerToggler(controller){
        controller.addEventListener('click', this.toggle.bind(this))
    }
}