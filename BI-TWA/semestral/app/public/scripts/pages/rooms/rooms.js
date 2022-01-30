(function() {
    const tableE = document.getElementById('table-rooms')
    const searchE = document.getElementById('search-form-rooms')
    const pagingE = document.getElementById('pages-rooms')

    const isAuth = tableE.getAttribute('data-auth') ?? false

    const detailAction = (elem, id) => {
        const detail = new UserDetail(elem, id)
        detail.detailAction()
    }

    const getAuthTable = () => {
        return new DataTableBuilder(tableE,'/api/rooms/?show-total-reservations=true&show-manager=true', '/room')
            .addGetterField('id', 'text', 'Id', (val)=>val["room"]["id"])
            .addActionField('name', 'link', 'Name',
                (val)=>val["room"]["name"],
                null,
                (elem, id) => {elem.setAttribute('href', '/room/' + id)})
            .addActionField('manager', 'link', 'Manager',
                (val) =>
                {
                    const manager = val.manager;
                    return (manager === null ? "No Manager" : manager.user.name)
                },
                (val) =>
                {
                    const manager = val.manager;
                    return (manager === null ? null : manager.user.id)
                },
                detailAction
            )
            .addGetterField('public', 'bool', 'Public', (val)=>val["room"]["public"])
            .addActionField('totalReservations', 'link', 'Reservations',
                (val)=>{return ((+val["totalReservations"] === 0) ? "no reservations" : `show [ ${val["totalReservations"]} ]`)},
                (val)=>val["room"]["id"],
                (elem, id) =>
                {
                    elem.setAttribute('href', `/room/${id}/reservations`)
                    elem.classList.add('button-link-light')
                }
            )
            .build()
    }


    const getUnAuthTable = () => {
        return new DataTableBuilder(tableE,'/api/rooms?show-total-reservations=true', '/room')
            // .addGetterField('id', 'text', 'Id')
            .addActionField('name', 'link', 'Name', (val)=>val["room"]["name"], (val)=>val["room"]["id"],
                (elem, id) => {elem.setAttribute('href', '/room/' + id)})
            .addActionField('building', 'link', 'Building', (val)=>val["room"]['building']['name'], (val)=>val["room"]['building']['id'], (elem, id)=>{elem.setAttribute('href', '/building/' + id)})
            .addActionField('totalReservations', 'link', 'Reservations',
                (val)=>{return ((+val["totalReservations"] === 0) ? "no reservations" : `show [ ${val["totalReservations"]} ]`)},
                (val)=>val["room"]["id"],
                (elem, id) =>
                {
                    elem.setAttribute('href', `/room/${id}/reservations`)
                    elem.classList.add('button-link-light')
                }
            )
            .build()
    }

    const roomTable = (!isAuth ? getUnAuthTable() : getAuthTable())


    roomTable.subscribe(new TablePaging(roomTable, pagingE))
    const search = new TableSearch(roomTable, searchE, ['name'])
    roomTable.subscribe(search)

    //todo: initial state for user detail - addeventlisteners on click
    roomTable.content.innerHTML = ''
    roomTable.load()
})()