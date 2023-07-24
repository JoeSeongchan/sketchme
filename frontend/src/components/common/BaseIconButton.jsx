import React from "react";
import { ReactComponent as MessageIcon } from '../../assets/icons/Message.svg'
import { ReactComponent as CalendarIcon } from "../../assets/icons/Calendar.svg"
import { ReactComponent as CancleIcon } from "../../assets/icons/Cancel.svg"
import { ReactComponent as CheckIcon } from "../../assets/icons/Check.svg"

const iconComponents = {
    message: MessageIcon,
    calendar: CalendarIcon,
    cancel: CancleIcon,
    check: CheckIcon
};

const BaseIconButton = ({icon, color}) => {
    const ActiveIconComponent = iconComponents[icon];
    return <button type="button" className="py-2 px-4flex justify-center items-center  bg-red-600 hover:bg-red-700 focus:ring-red-500 focus:ring-offset-red-200 text-white w-full transition ease-in duration-200 text-center text-base font-semibold shadow-md focus:outline-none focus:ring-2 focus:ring-offset-2  rounded-lg ">
        <ActiveIconComponent fill={color} width="50px" height="50px"></ActiveIconComponent>
        Upload
    </button>
        ;
}

export default BaseIconButton;