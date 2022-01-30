// Helper function for creating table cells
const createCell = (content) => {
    let cell = document.createElement("td")
    cell.innerHTML = content
    return cell
}

// Helper function for creating image element
const createImage = (employee, photo) => {
    let img = document.createElement("img")
    img.alt = "Profile image for " + employee
    img.src = "employees/" + photo
    img.width = 100
    return img
}

// Helper function for creating links in table cells
const createLink = (route, employee, text) => {
    let a = document.createElement("a")
    a.href = Routing.generate(route, { 'username': employee })
    a.innerText = text
    return a
}

// Helper function for creating e-mail link
const createMailLink = (email) => {
    let a = document.createElement("a")
    a.href = "mailto:" + email
    a.innerText = email
    return a
}

// Helper function for parsing roles
const createRoleSpan = (roles) => {
    let span = document.createElement("span")
    if (roles.length === 0) {
        span.classList.add("cursive")
        span.innerText = "No positions"
        return span
    }

    span.innerText =  roles.map(role => role.name).join(', ')
    return span
}

// Reload employee list
const reloadEmployees = (filterName = "") => fetch("/api/employee/?name=" + filterName)
    .then(async function (response) {
        // Unsuccessful
        if (response.status !== 200) return

        // Get response
        let employees = JSON.parse(await response.text())

        // Clear table
        let table = document.querySelector("table")
        while (table.firstChild !== null) table.firstChild.remove()

        // No employees = display just error message
        console.log(employees)
        console.log(employees.length)
        if (employees.length === 0) {
            table.innerHTML = "<tr><td>" +
                (filterName === "" ? "There are currently no employees"
                    : "There are no employees matching current search.") +
                "</td></tr>"
            return
        }

        // Add head
        let thead = document.createElement("thead")
        thead.innerHTML = "<tr>\n" +
            "                <th>Photo</th>\n" +
            "                <th>Name</th>\n" +
            "                <th>Position</th>\n" +
            "                <th>Email</th>\n" +
            "                <th></th>\n" +
            "                <th></th>" +
            "            </tr>"
        table.append(thead)

        let tbody = document.createElement("tbody")

        for (let employee of employees) {
            let tr = document.createElement("tr")
            tr.append(createCell(
                employee.can_view ?
                createImage(employee.username, employee.photo).outerHTML
                    : "Private"
            ))
            tr.append(createCell(
                employee.can_view ?
                    createLink("employee_detail", employee.username, employee.name).outerHTML
                    : employee.name
            ))
            tr.append(createCell(createRoleSpan(employee.roles).outerHTML))
            tr.append(createCell(createMailLink(employee.email).outerHTML))
            if (employee.can_edit) {
                tr.append(createCell(createLink("employee_edit", employee.username, "Edit").outerHTML))
                tr.append(createCell(createLink("employee_delete", employee.username, "Delete").outerHTML))
            }
            else {
                tr.append(document.createElement("td"))
                tr.append(document.createElement("td"))
            }

            tbody.append(tr)
        }
        table.append(tbody)
    });

document.addEventListener('DOMContentLoaded', () => {
    // Set search binding
    let input = document.querySelector("input#search")
    input.addEventListener('input', (event) => reloadEmployees(event.target.value))

    // Load employees
    reloadEmployees()
})
