(function() {
    const tableE = document.getElementById('table-room-users-add')
    const roomId = tableE.getAttribute('data-room-id')
    const searchE = document.getElementById('search-form-room-users-add')
    const pagingE = document.getElementById('pages-room-users-add')

    const isAuthAdmin = tableE.getAttribute('data-auth-admin') ?? false

    const addAction = (id, values) => {
        addRoomUsersTable.loader.start()

        fetch(`/api/roomusers`,
            {
                method: 'POST',
                headers: {
                    'Accept': 'application/json',
                    'Content-Type': 'application/json',
                    'credentials': 'same-origin'
                },
                body: JSON.stringify(values)
            })
            .then(response => {
                if (response.status === 204){
                    console.log("ADDED")
                }
                addRoomUsersTable.load()
                console.log(response)
            })
            .catch(err => {
                console.log(err.message)
                addRoomUsersTable.loader.stop()
            })
    }

    const add = (manager) => {
        return (elem, id) => {
            elem.addEventListener('click', () => {
                const values = {
                    'user': id,
                    'room': roomId,
                    'manager': manager
                }
                addAction(id, values)
            })
        }
    }

    const detailAction = (elem, id) => {
        const detail = new UserDetail(elem, id)
        detail.detailAction()
    }


    const addRoomUsersTableBuilder =
        new DataTableBuilder(tableE,`/api/rooms/${roomId}/users?show-members=false`, `/room/${roomId}/addUsers`)
            .addField( 'id', 'text', 'Id')

    if(isAuthAdmin){
        addRoomUsersTableBuilder
            .addActionField('name', 'link', 'Name',
                null,
                null,
                detailAction
            )
    }
    else{
        addRoomUsersTableBuilder
            .addField( 'name', 'text', 'Name')
    }

    addRoomUsersTableBuilder
        .addActionField('add-user', 'button', 'Add Member', null, (item) => item["id"], add(false))
        .addActionField('add-manager', 'button', 'Add Manager', null, (item) => item["id"], add(true))


    const addRoomUsersTable = addRoomUsersTableBuilder.build()


    addRoomUsersTable.subscribe(new TablePaging(addRoomUsersTable, pagingE))
    const search = new TableSearch(addRoomUsersTable, searchE, ['name'])
    addRoomUsersTable.subscribe(search)

    //todo: initial state for buttons - addeventlisteners on click
    addRoomUsersTable.content.innerHTML = ''
    addRoomUsersTable.load()
})()