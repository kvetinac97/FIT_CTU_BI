(function() {
    const tableE = document.getElementById('table-room-users')
    const roomId = tableE.getAttribute('data-room-id')
    const searchE = document.getElementById('search-form-room-users')
    const pagingE = document.getElementById('pages-room-users')

    const isAuthAdmin = tableE.getAttribute('data-auth-admin') ?? false

    const removeAction = (id) => {
        roomUsersTable.loader.start()
        fetch(`/api/roomusers/${id}`,
            {method: 'DELETE'})
            .then(response => {
                if (response.status === 204){
                    console.log("REMOVED")
                }
                roomUsersTable.load()
                console.log(response)
            })
            .catch(err => {
                console.log(err.message)
                roomUsersTable.loader.stop()
            })
    }

    const remove = (elem, id) => { elem.addEventListener('click', () => {removeAction(id)}) }


    const changeAction = (id, values) => {
        roomUsersTable.loader.start()
        fetch(`/api/roomusers/${id}`,
            {
                    method: 'PATCH',
                    headers: {
                        'Accept': 'application/json',
                        'Content-Type': 'application/json',
                        'credentials': 'same-origin'
                    },
                    body: JSON.stringify(values)
                }
            )
            .then(response => {
                if (response.status === 204){
                    console.log("CHANGED")
                }
                console.log(response)
                return response.json()
            })
            .then(data => {
                console.log((data))
                roomUsersTable.load()
            })
            .catch(err => {
                console.log(err.message)
                roomUsersTable.loader.stop()
            })
    }

    const change = (elem, id, item) => { elem.addEventListener('click', () => {
        const manager = item["manager"]
        changeAction(id, {'manager' : !manager})
    }) }

    const detailAction = (elem, id) => {
        const detail = new UserDetail(elem, id)
        detail.detailAction()
    }


    const roomUsersTableBuilder =
        new DataTableBuilder(tableE,`/api/rooms/${roomId}/users`, `/room/${roomId}/users`)
            .addGetterField( 'id', 'text', 'Id', (item) => item["user"]["id"])

    if(isAuthAdmin){
        roomUsersTableBuilder
            .addActionField('name', 'link', 'Name',
                (item) => item["user"]["name"],
                (item) => item["user"]["id"],
                detailAction
            )
    }
    else{
        roomUsersTableBuilder
            .addGetterField( 'name', 'text', 'Name', (item) => item["user"]["name"])
    }

    roomUsersTableBuilder
        .addField('manager', 'bool', 'Manager')
        .addActionField('remove', 'button', 'Remove', null, (item) => item["id"], remove)
        .addActionField('change', 'button', 'Change role', null, (item) => item["id"], change)

    const roomUsersTable = roomUsersTableBuilder.build()

    roomUsersTable.subscribe(new TablePaging(roomUsersTable, pagingE))
    const search = new TableSearch(roomUsersTable, searchE, ['name'])
    roomUsersTable.subscribe(search)

    //todo: initial state for buttons - addeventlisteners on click
    roomUsersTable.content.innerHTML = ''
    roomUsersTable.load()
})()