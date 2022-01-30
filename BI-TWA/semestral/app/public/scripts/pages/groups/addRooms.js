(function() {
    const tableE = document.getElementById('table-group-rooms-add')
    const groupId = tableE.getAttribute('data-group-id')
    const searchE = document.getElementById('search-form-group-rooms-add')
    const pagingE = document.getElementById('pages-group-rooms-add')

    const addToGroupAction = (id, values) => {
        addGroupRoomsTable.loader.start()

        fetch(`/api/rooms/${id}`,
            {
                method: 'PUT',
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
                addGroupRoomsTable.load()
                console.log(response)
            })
            .catch(err => {
                console.log(err.message)
                addGroupRoomsTable.loader.stop()
            })
    }

    const add = (elem, id) => {
        elem.addEventListener('click', () => {
            const values = {
                'group': groupId,
            }
            addToGroupAction(id, values)
        })
    }


    const addGroupRoomsTable =
        new DataTableBuilder(tableE,`/api/groups/${groupId}/rooms?show-members=false`, `/group/${groupId}/addRooms`)
            .addField( 'id', 'text', 'Id')
            .addField( 'name', 'text', 'Name')
            .addActionField('add', 'button', 'Add To Group', null, (item) => item["id"], add)
            .build()

    addGroupRoomsTable.subscribe(new TablePaging(addGroupRoomsTable, pagingE))
    const search = new TableSearch(addGroupRoomsTable, searchE, ['name'])
    addGroupRoomsTable.subscribe(search)

    //todo: initial state for buttons - addeventlisteners on click
    addGroupRoomsTable.content.innerHTML = ''
    addGroupRoomsTable.load()
})()