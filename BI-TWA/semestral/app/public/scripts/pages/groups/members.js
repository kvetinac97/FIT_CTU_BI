(function() {
    const tableE = document.getElementById('table-group-members')
    const groupId = tableE.getAttribute('data-group-id')
    const searchE = document.getElementById('search-form-group-users')
    const pagingE = document.getElementById('pages-group-members')

    const isAuthAdmin = tableE.getAttribute('data-auth-admin') ?? false

    const detailAction = (elem, id) => {
        const detail = new UserDetail(elem, id)
        detail.detailAction()
    }


    const removeAction = (id) => {
        groupMembersTable.loader.start()
        fetch(`/api/members/${id}`,
            {method: 'DELETE'})
            .then(response => {
                if (response.status === 204){
                    console.log("REMOVED")
                }
                groupMembersTable.load()
                console.log(response)
            })
            .catch(err => {
                console.log(err.message)
                groupMembersTable.loader.stop()
            })
    }

    const remove = (elem, id) => { elem.addEventListener('click', () => {removeAction(id)}) }


    const changeAction = (id, values) => {
        groupMembersTable.loader.start()
        fetch(`/api/members/${id}`,
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
                groupMembersTable.load()
            })
            .catch(err => {
                console.log(err.message)
                groupMembersTable.loader.stop()
            })
    }

    const change = (elem, id, item) => { elem.addEventListener('click', () => {
        const manager = item["manager"]
        changeAction(id, {'manager' : !manager})
    }) }


    const groupMembersTableBuilder =
        new DataTableBuilder(tableE,`/api/groups/${groupId}/users`, `/group/${groupId}/users`)
            .addGetterField( 'id', 'text', 'Id', (item) => item["user"]["id"])

    if(isAuthAdmin){
        groupMembersTableBuilder
            .addActionField('name', 'link', 'Name',
                (item) => item["user"]["name"],
                (item) => item["user"]["id"],
                detailAction
            )
    }
    else{
        groupMembersTableBuilder
            .addGetterField( 'name', 'text', 'Name', (item) => item["user"]["name"])
    }

    groupMembersTableBuilder
        .addField('manager', 'bool', 'Manager')
        .addActionField('remove', 'button', 'Remove', null, (item) => item["id"], remove)
        .addActionField('change', 'button', 'Change role', null, (item) => item["id"], change)

    const groupMembersTable = groupMembersTableBuilder.build()

    groupMembersTable.subscribe(new TablePaging(groupMembersTable, pagingE))
    const search = new TableSearch(groupMembersTable, searchE, ['name'])
    groupMembersTable.subscribe(search)

    //todo: initial state for buttons - addeventlisteners on click
    groupMembersTable.content.innerHTML = ''
    groupMembersTable.load()
})()