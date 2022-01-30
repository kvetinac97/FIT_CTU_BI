(function() {
    const tableE = document.getElementById('table-reservation-users-add')
    const reservationId = tableE.getAttribute('data-reservation-id')
    const searchE = document.getElementById('search-form-reservation-users-add')
    const pagingE = document.getElementById('pages-reservation-users-add')

    const isAuthAdmin = tableE.getAttribute('data-auth-admin') ?? false

    const detailAction = (elem, id) => {
        const detail = new UserDetail(elem, id)
        detail.detailAction()
    }

    const addAction = (id) => {
        addReservationUsersTable.loader.start()

        fetch(`/api/reservations/${reservationId}/users?action=ADD&users=[${id}]`,
            {
                method: 'PUT',
                headers: {
                    'Accept': 'application/json',
                    'Content-Type': 'application/json',
                    'credentials': 'same-origin'
                }})
            .then(response => {
                if (response.status === 204){
                    console.log("ADDED")
                }
                addReservationUsersTable.load()
                console.log(response)
            })
            .catch(err => {
                console.log(err.message)
                addReservationUsersTable.loader.stop()
            })
    }

    const add = (elem, id) => { elem.addEventListener('click', () => {
        addAction(id)
    }) }


    const addReservationUsersTableBuilder =
        new DataTableBuilder(tableE,`/api/reservations/${reservationId}/users?show-members=false`, `/reservation/${reservationId}/addUsers`)
            .addField( 'id', 'text', 'Id')

    if(isAuthAdmin){
        addReservationUsersTableBuilder
            .addActionField('name', 'link', 'Name',
                null,
                null,
                detailAction
            )
    }
    else{
        addReservationUsersTableBuilder
            .addField( 'name', 'text', 'Name')
    }

    addReservationUsersTableBuilder
            .addActionField('add', 'button', 'Add', null, (item) => item["id"], add)


    const addReservationUsersTable = addReservationUsersTableBuilder.build()


    const search = new TableSearch(addReservationUsersTable, searchE, ['name'])
    addReservationUsersTable.subscribe(search)
    addReservationUsersTable.subscribe(new TablePaging(addReservationUsersTable, pagingE))

    //todo: initial state for buttons - addeventlisteners on click
    addReservationUsersTable.content.innerHTML = ''
    addReservationUsersTable.load()
})()