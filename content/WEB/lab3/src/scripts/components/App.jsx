import './../../resources/style/components/App.css';

import Patient from './Patient';

function App() {
  return (
    <div className="App">
      <Patient 
        fullName="Прокопенко Дмитрий Олегович" 
        phoneNumber="+7 (951) 500-81-74" 
        height="171" 
        weight="64" 
        sex="Мужской" 
      />
      <Patient 
        fullName="Вискова Екатерина Алексеевна" 
        phoneNumber="+31 (666) 228-13-37" 
        height="120" 
        weight="120" 
        sex="Ж" 
      />
      <Patient 
        fullName="Поркшеян Виталий Маркосович" 
        phoneNumber="ДАННЫЕ УДАЛЕНЫ" 
        height="ДАННЫЕ УДАЛЕНЫ" 
        weight="ДАННЫЕ УДАЛЕНЫ" 
        sex="Бог" 
      />
    </div>
  );
}

export default App;
