import { Wrap, WrapItem, Spinner, Text } from '@chakra-ui/react';
import SideBarWithHeader from './components/shared/SideBar.jsx';
import {useEffect, useState} from 'react';
import {getCustomers} from './services/client.js';
import CardWithImage from './components/Card';

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
            <Wrap justify={"center"} spacing={"30px"}>
                {customers.map((customer, index) => (
                    <WrapItem key={index}>
                        <CardWithImage
                            {...customer}
                            imageNumber={index}
                        />
                    </WrapItem>

                ))}
            </Wrap>

        </SideBarWithHeader>
    )
}

export default App;
