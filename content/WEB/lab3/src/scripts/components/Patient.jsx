import './../../resources/style/components/Patient.css';

function Patient({fullName, phoneNumber, height, weight, sex}) {
    return (
        <div className="Patient">
            <p><b>ФИО:</b> {fullName}</p>
            <p><b>Номер:</b> {phoneNumber}</p>
            <p><b>Рост:</b> {height}</p>
            <p><b>Вес:</b> {weight}</p>
            <p><b>Пол:</b> {sex}</p>
        </div>
    );
}

export default Patient