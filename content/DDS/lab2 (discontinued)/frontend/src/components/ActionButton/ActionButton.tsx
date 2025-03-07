import { JSX } from 'react'

import { ActionButtonProps } from '../../types/props/ActionButtonProps.ts'

import './ActionButton.scss'

export default function ActionButton({ label, action, customCSS }: ActionButtonProps): JSX.Element {
    return (
        <button className='button-action' style={ customCSS } onClick={ action }> { label } </button>
    )
}