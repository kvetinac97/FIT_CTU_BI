(()=>{
    const subNavE = document.getElementById('sub-nav')
    const subMenuE = subNavE.querySelector('.sub-menu')
    const subMenuControllerE = subNavE.querySelector('.sub-menu-controller')

    const subMenu = new SubMenu(subMenuE, subMenuControllerE)
    subMenu.close()
})()