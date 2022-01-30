(function() {
    const tableE = document.getElementById('table-reservation-users')
    const reservationId = tableE.getAttribute('data-reservation-id')
    const searchE = document.getElementById('search-form-reservation-users')
    const pagingE = document.getElementById('pages-reservation-users')

    const isAuthRemove = tableE.getAttribute('data-auth-edit') ?? false
    const isAuthAdmin = tableE.getAttribute('data-auth-admin') ?? false

    const detailAction = (elem, id) => {
        const detail = new UserDetail(elem, id)
        detail.detailAction()
    }

    const removeAction = (id) => {
        reservationUsersTable.loader.start()

        fetch(`/api/reservations/${reservationId}/users?action=DELETE&users=[${id}]`,
            {
                method: 'PUT',
                headers: {
                    'Accept': 'application/json',
                    'Content-Type': 'application/json',
                    'credentials': 'same-origin'
                }})
            .then(response => {
                if (response.status === 204){
                    console.log("REMOVED")
                }
                reservationUsersTable.load()
                console.log(response)
            })
            .catch(err => {
                console.log(err.message)
                reservationUsersTable.loader.stop()
            })
    }

    const remove = (elem, id) => { elem.addEventListener('click', () => {
        removeAction(id)
    }) }


    const reservationUsersTableBuilder =
        new DataTableBuilder(tableE,`/api/reservations/${reservationId}/users`, `/reservation/${reservationId}/users`)
            .addField( 'id', 'text', 'Id')


    if(isAuthAdmin){
        reservationUsersTableBuilder
            .addActionField('name', 'link', 'Name',
                null,
                null,
                detailAction
            )
    }
    else{
        reservationUsersTableBuilder
            .addField( 'name', 'text', 'Name')
    }

    if(isAuthRemove){
        reservationUsersTableBuilder
            .addActionField('remove', 'button', 'Remove', null, (item) => item["id"], remove)
    }

    const reservationUsersTable = reservationUsersTableBuilder.build()

    const search = new TableSearch(reservationUsersTable, searchE, ['name'])
    reservationUsersTable.subscribe(search)
    reservationUsersTable.subscribe(new TablePaging(reservationUsersTable, pagingE))

    //todo: initial state for buttons - addeventlisteners on click
    reservationUsersTable.content.innerHTML = ''
    reservationUsersTable.load()
})()