// Helper function for creating table cells
const createCell = (content) => {
    let cell = document.createElement("td")
    cell.innerHTML = content
    return cell
}

// Helper function for creating links in table cells
const createLink = (route, employee, name, text) => {
    let a = document.createElement("a")
    a.href = Routing.generate(route, { 'username': employee, 'name': name })
    a.innerText = text
    return a
}

// Helper function for formatting date
const formatDate = (dateString) => {
    if (dateString === undefined) return "Never"
    let date = new Date(dateString)
    date.setMinutes(date.getMinutes() + date.getTimezoneOffset())
    const options = { year: 'numeric', month: '2-digit', day: '2-digit', hour: '2-digit', minute: '2-digit' };
    return date.toLocaleString(undefined, options)
}

// Reload account list
const reloadAccounts = (employee) => fetch("/api/employee/" + employee + "/account")
    .then(async function (response) {
        // Unsuccessful = reload page
        if (response.status !== 200) {
            window.location.reload()
            return
        }

        // Get response
        let accounts = JSON.parse(await response.text())

        // Clear table
        let table = document.querySelector("table")
        while (table.firstChild !== null) table.firstChild.remove()

        // No employees = display just error message
        console.log(accounts)
        console.log(accounts.length)
        if (accounts.length === 0) {
            table.innerHTML = "<tr><td>This user has no accounts.</td></tr>"
            return
        }

        // Add head
        let thead = document.createElement("thead")
        thead.innerHTML = "<tr>\n" +
            "                <th>#</th>\n" +
            "                <th>Name</th>\n" +
            "                <th>Expiration</th>\n" +
            "                <th></th>\n" +
            "                <th></th>\n" +
            "            </tr>"
        table.append(thead)

        let tbody = document.createElement("tbody")
        let dialog = new DeleteDialog(document.querySelector("#confirm"))

        for (let account of accounts) {
            let tr = document.createElement("tr")
            tr.append(createCell(account.id))
            tr.append(createCell(account.name))
            tr.append(createCell(formatDate(account.expiration)))
            tr.append(account.can_edit ?
                createCell(createLink("account_edit", employee, account.name, "Edit").outerHTML)
                : document.createElement("td"))

            let delLink = createLink("account_delete", employee, account.name, "Delete")
            delLink.dataset.ajaxDelete = account.name
            delLink.dataset.confirm = "Do you really want to delete " + account.name + "?"
            let td = createCell(delLink.outerHTML)
            dialogToggle(td.querySelector("[data-ajax-delete]"), dialog)

            tr.append(account.can_edit ? td : document.createElement("td"))
            tbody.append(tr)
        }
        table.append(tbody)
    })
    // Could not fetch => reload
    .catch((err) => window.location.reload());

// Create body of request
const stringFromForm = (data) => {
    let it = data.entries(), val, str = ""
    do {
        if (val) str += val.value[0].replace(new RegExp("account\[.*\]"), new RegExp("$1")) + "=" + val.value[1] + "&"
        val = it.next()
    }
    while (!val.done)
    return str;
}

// Create new account / show errors
const submitForm = (employee, data) => fetch( "/api/employee/" + employee + "/account", {
    method: 'POST',
    headers: {
        'Content-type': 'application/x-www-form-urlencoded'
    },
    body: stringFromForm(data)
} )
    .then(async function (response) {
        // Clean errors
        let fr = document.querySelector("div.form-row.error")
        while ( fr.firstChild !== null ) fr.firstChild.remove()

        // Success, reload
        if (response.status === 201) {
            await reloadAccounts(employee);
            return response
        }

        // Show error
        let res
        try { res = JSON.parse(await response.text()).message }
        catch (e) { res = "Unknown error" }

        if (response.status === 403) res = "Unauthorized"

        let ul = document.createElement('ul')
        let li = document.createElement('li');
        li.innerText = res
        ul.append(li)
        fr.append(ul)
        return response
    });

// Delete account
const deleteAccount = (employee, account, fallback) => fetch("/api/employee/" + employee + "/account/" + account, {
    method: 'DELETE'
})
    .then(async function (response) {
        console.log(response)
        // Unsuccessful = fallback
        if (response.status !== 204) {
            window.location.href = fallback
            return
        }

        // Success = reload list
        await reloadAccounts(employee)
    })
    .catch((err) => window.location.href = fallback)

// Class representing dialog
class DeleteDialog {
    constructor (dialog) {
        this.dialog = dialog
        this.hidden = true
    }

    show(link) {
        this.hidden = false
        this.dialog.querySelector("#confirm-title").innerText = link.dataset.confirm

        const yes = this.dialog.querySelector("#confirm-yes")
        const fn = () => {
            deleteAccount(username, link.dataset.ajaxDelete, link.href)
            yes.removeEventListener('click', fn)
            this.hide()
        }
        yes.addEventListener('click', fn)

        this.dialog.querySelector("#confirm-no").addEventListener('click', () => {
            yes.removeEventListener('click', fn)
            this.hide()
        })
        this.dialog.classList.add('is-visible')
    }

    hide() {
        this.hidden = true
        this.dialog.classList.remove('is-visible')
    }
}

// Dialog registration
const dialogToggle = (elem, dialog) => {
    elem.addEventListener('click', (event) => {
        if (dialog.hidden) dialog.show(elem)
        else dialog.hide()
        event.preventDefault()
    })
}

// Submit form with AJAX
let submitListener = (event) => {
    let data = new FormData(event.target)
    event.preventDefault()
    submitForm(username, data).then(r => { if (r.ok) event.target.reset() })
}

document.addEventListener('DOMContentLoaded', () => {
    let form = document.querySelector("form[name='account']")
    if (form) form.addEventListener('submit', submitListener)

    // Load employees
    reloadAccounts(username)
})
