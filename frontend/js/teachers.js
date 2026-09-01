// آدرس پایه بک‌اند
const BASE_URL = 'http://localhost:8080';

const errorBox = document.getElementById('errorBox');

const formErrorBox = document.getElementById('formErrorBox');

const saveTeacherBtn = document.getElementById('saveTeacherBtn');

const addTeacherBtn = document.getElementById('addTeacherBtn');

const teacherModalEl = document.getElementById('teacherModal');

const teacherModalLabel = document.getElementById('teacherModalLabel');

const teacherModal = new bootstrap.Modal(teacherModalEl);

const teachersTableBody = document.getElementById('teachersTableBody');

const searchInput = document.getElementById('searchInput');

// این متغیر «حافظه‌ی حالت فعلی Modal» هست:
//
// null = Create
// عدد = id استادی که در حال ویرایش آن هستیم

let editingTeacherId = null;

// وقتی دکمه «افزودن استاد» کلیک می‌شود
// فرم را وارد حالت Create می‌کنیم

addTeacherBtn.addEventListener('click', () => {

editingTeacherId = null;

teacherModalLabel.textContent = 'افزودن استاد';

document.getElementById('teacherForm').reset();

hideFormError();

});

// وقتی دکمه «ذخیره» کلیک می‌شود
//
// اگر id نداشته باشیم → Create
// اگر id داشته باشیم → Update

saveTeacherBtn.addEventListener('click', () => {

if (editingTeacherId === null) {

```
createTeacher();
```

} else {

```
updateTeacher(editingTeacherId);
```

}

});

// وقتی صفحه کامل لود شد
// لیست اساتید را از API می‌گیریم

document.addEventListener('DOMContentLoaded', () => {

loadTeachers();

});

// Event Delegation
//
// چون دکمه‌های Edit و Delete بعداً توسط JavaScript ساخته می‌شوند،
// Listener را روی خود جدول قرار می‌دهیم.

teachersTableBody.addEventListener('click', (event) => {

if (event.target.classList.contains('btn-edit')) {

```
const id = event.target.dataset.id;

openEditModal(id);
```

}

if (event.target.classList.contains('btn-delete')) {

```
const id = event.target.dataset.id;

const isConfirmed = confirm(
  'آیا از حذف این استاد مطمئن هستید؟'
);

if (isConfirmed) {

  deleteTeacher(id);

}
```

}

});

// ---------- GET /teacher : گرفتن لیست همه اساتید ----------

function loadTeachers() {

hideListError();

fetch(`${BASE_URL}/teacher`, {

```
method: 'GET'
```

})

```
.then((response) => {

  if (!response.ok) {

    throw new Error(
      `خطای سرور: ${response.status}`
    );

  }

  return response.json();

})

.then((teachers) => {

  renderTeachers(teachers);

})

.catch((error) => {

  showListError(error.message);

});
```

}

// ---------- نمایش اساتید داخل جدول ----------

function renderTeachers(teachers) {

teachersTableBody.innerHTML = '';

if (teachers.length === 0) {

```
teachersTableBody.innerHTML = `
  <tr>
    <td colspan="6" class="text-center text-muted">
      هیچ استادی ثبت نشده
    </td>
  </tr>
`;

return;
```

}

teachers.forEach((teacher) => {

```
const row = document.createElement('tr');

row.innerHTML = `
  <td>${teacher.id}</td>

  <td>${teacher.firstName}</td>

  <td>${teacher.lastName}</td>

  <td>${teacher.email}</td>

  <td>${teacher.specialty}</td>

  <td>

    <button
      class="btn btn-sm btn-warning btn-edit"
      data-id="${teacher.id}">
      ویرایش
    </button>

    <button
      class="btn btn-sm btn-danger btn-delete"
      data-id="${teacher.id}">
      حذف
    </button>

  </td>
`;

teachersTableBody.appendChild(row);
```

});

}

// ---------- GET /teacher/{id} : گرفتن اطلاعات یک استاد ----------

function openEditModal(id) {

hideFormError();

fetch(`${BASE_URL}/teacher/${id}`, {

```
method: 'GET'
```

})

```
.then((response) => {

  if (!response.ok) {

    throw new Error(
      `خطای سرور: ${response.status}`
    );

  }

  return response.json();

})

.then((teacher) => {

  // فرم را با اطلاعات فعلی استاد پر می‌کنیم

  document.getElementById('firstName').value =
    teacher.firstName;

  document.getElementById('lastName').value =
    teacher.lastName;

  document.getElementById('email').value =
    teacher.email;

  document.getElementById('specialty').value =
    teacher.specialty;


  // از این لحظه Modal در حالت Update است

  editingTeacherId = teacher.id;

  teacherModalLabel.textContent = 'ویرایش استاد';

  teacherModal.show();

})

.catch((error) => {

  showListError(error.message);

});
```

}

// ---------- POST /teacher : ساخت استاد ----------

function createTeacher() {

const requestBody = {

```
firstName: document.getElementById('firstName').value,

lastName: document.getElementById('lastName').value,

email: document.getElementById('email').value,

specialty: document.getElementById('specialty').value
```

};

hideFormError();

fetch(`${BASE_URL}/teacher`, {

```
method: 'POST',

headers: {

  'Content-Type': 'application/json'

},

body: JSON.stringify(requestBody)
```

})

```
.then(async (response) => {

  if (!response.ok) {

    const errorData =
      await response.json().catch(() => null);

    throw new Error(
      errorData
        ? JSON.stringify(errorData)
        : `خطای سرور: ${response.status}`
    );

  }

  return response.json();

})

.then((createdTeacher) => {

  console.log(
    'استاد ساخته شد:',
    createdTeacher
  );

  teacherModal.hide();

  document.getElementById('teacherForm').reset();

  editingTeacherId = null;

  loadTeachers();

})

.catch((error) => {

  showFormError(error.message);

});
```

}

// ---------- PUT /teacher/{id} : ویرایش استاد ----------

function updateTeacher(id) {

const requestBody = {

```
firstName: document.getElementById('firstName').value,

lastName: document.getElementById('lastName').value,

email: document.getElementById('email').value,

specialty: document.getElementById('specialty').value
```

};

hideFormError();

fetch(`${BASE_URL}/teacher/${id}`, {

```
method: 'PUT',

headers: {

  'Content-Type': 'application/json'

},

body: JSON.stringify(requestBody)
```

})

```
.then(async (response) => {

  if (!response.ok) {

    const errorData =
      await response.json().catch(() => null);

    throw new Error(
      errorData
        ? JSON.stringify(errorData)
        : `خطای سرور: ${response.status}`
    );

  }

  return response.json();

})

.then((updatedTeacher) => {

  console.log(
    'استاد ویرایش شد:',
    updatedTeacher
  );

  teacherModal.hide();

  document.getElementById('teacherForm').reset();

  editingTeacherId = null;

  loadTeachers();

})

.catch((error) => {

  showFormError(error.message);

});
```

}

// ---------- DELETE /teacher/{id} : حذف استاد ----------

function deleteTeacher(id) {

hideListError();

fetch(`${BASE_URL}/teacher/${id}`, {

```
method: 'DELETE'
```

})

```
.then((response) => {

  if (!response.ok) {

    throw new Error(
      `خطای سرور: ${response.status}`
    );

  }

  // Backend با 204 No Content پاسخ می‌دهد
  // بنابراین response.json() نمی‌زنیم.

  loadTeachers();

})

.catch((error) => {

  showListError(error.message);

});
```

}

// ---------- GET /teacher/search?specialty= ----------

function searchTeachers() {

const specialty = searchInput.value.trim();

// اگر چیزی برای جستجو وارد نشده،
// دوباره همه اساتید را بگیر.

if (specialty === '') {

```
loadTeachers();

return;
```

}

hideListError();

fetch(
`${BASE_URL}/teacher/search?specialty=${encodeURIComponent(specialty)}`,
{
method: 'GET'
}
)

```
.then((response) => {

  if (!response.ok) {

    throw new Error(
      `خطای سرور: ${response.status}`
    );

  }

  return response.json();

})

.then((teachers) => {

  renderTeachers(teachers);

})

.catch((error) => {

  showListError(error.message);

});
```

}

// جستجو هنگام تایپ
//
// برای اینکه مثل Course فقط یک input ساده داشته باشیم،
// با Enter جستجو انجام می‌شود.

searchInput.addEventListener('keydown', (event) => {

if (event.key === 'Enter') {

```
searchTeachers();
```

}

});

// ---------- ERROR UI ----------

function showListError(message) {

errorBox.textContent = message;

errorBox.classList.remove('d-none');

}

function hideListError() {

errorBox.classList.add('d-none');

errorBox.textContent = '';

}

function showFormError(message) {

formErrorBox.textContent = message;

formErrorBox.classList.remove('d-none');

}

function hideFormError() {

formErrorBox.classList.add('d-none');

formErrorBox.textContent = '';

}
