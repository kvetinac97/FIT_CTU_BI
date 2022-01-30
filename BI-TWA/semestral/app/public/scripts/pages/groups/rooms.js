(function() {
    const tableE = document.getElementById('table-group-rooms')
    const groupId = tableE.getAttribute('data-group-id')
    const searchE = document.getElementById('search-form-group-rooms')
    const pagingE = document.getElementById('pages-group-rooms')

    const isAuthAdmin = tableE.getAttribute('data-auth-admin') ?? false

    const changeAction = (id, values) => {
        groupRoomsTable.loader.start()

        fetch(`/api/rooms/${id}`,
            {
                    method: 'PUT',
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
                groupRoomsTable.load()
            })
            .catch(err => {
                console.log(err.message)
                groupRoomsTable.loader.stop()
            })
    }

    const remove = (elem, id) => { elem.addEventListener('click', () => {
        changeAction(id, {'group' : null})
    }) }


    const groupRoomsTableBuilder =
        new DataTableBuilder(tableE,`/api/rooms/?group=${groupId}`, `/group/${groupId}/rooms`)
            .addField( 'id', 'text', 'Id')
            .addActionField( 'name', 'link', 'Name', null, null, (elem, id) => {elem.setAttribute('href', `/rooms/${id}`)})

    if(isAuthAdmin){
        groupRoomsTableBuilder
            .addActionField('remove', 'button', 'Remove', null, (item) => item["id"], remove)
    }

    const groupRoomsTable = groupRoomsTableBuilder.build()

    groupRoomsTable.subscribe(new TablePaging(groupRoomsTable, pagingE))
    const search = new TableSearch(groupRoomsTable, searchE, ['name'])
    groupRoomsTable.subscribe(search)

    //todo: initial state for buttons - addeventlisteners on click
    groupRoomsTable.content.innerHTML = ''
    groupRoomsTable.load()
})()