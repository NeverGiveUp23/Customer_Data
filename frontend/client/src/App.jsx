import { Button, Spinner, Text } from '@chakra-ui/react';
import SideBarWithHeader from './shared/SideBar.jsx';
import {useEffect, useState} from 'react';
import {getCustomers} from './services/client.js';

const App = ()  => {
    const [ customers, setCustomers] = useState([]);
    const [ isLoading, setIsLoading] = useState(false);

    useEffect(() => {
        setIsLoading(true)
        setTimeout(() => {
            getCustomers().then((res) => {
                setCustomers(res.data);
            }).catch((err) => {
                console.log(err);
            }).finally(() => {
                setIsLoading(false);
            })
        }, 3000);
    }, []);

        if(isLoading){
          return ( <SideBarWithHeader>
                <Spinner />
            </SideBarWithHeader>)
        }

        if(customers.length <= 0){
            return (
                <SideBarWithHeader>
                    <Text>No Customers Found!</Text>
                </SideBarWithHeader>
            )
        }

    return (
        <SideBarWithHeader>
            {customers.map((customer, index) => {
                return (
                    <div>
                        <Text>{customer.name}</Text>
                        <Text>{customer.age}</Text>
                        <Text>{customer.email}</Text>
                    </div>
                )
            })}
        </SideBarWithHeader>
    )
}

export default App;
