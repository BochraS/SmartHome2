import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { ISmartObject } from 'app/shared/model/smart-object.model';
import { SmartObjectService } from './smart-object.service';

@Component({
    selector: 'jhi-smart-object-delete-dialog',
    templateUrl: './smart-object-delete-dialog.component.html'
})
export class SmartObjectDeleteDialogComponent {
    smartObject: ISmartObject;

    constructor(
        protected smartObjectService: SmartObjectService,
        public activeModal: NgbActiveModal,
        protected eventManager: JhiEventManager
    ) {}

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.smartObjectService.delete(id).subscribe(response => {
            this.eventManager.broadcast({
                name: 'smartObjectListModification',
                content: 'Deleted an smartObject'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-smart-object-delete-popup',
    template: ''
})
export class SmartObjectDeletePopupComponent implements OnInit, OnDestroy {
    protected ngbModalRef: NgbModalRef;

    constructor(protected activatedRoute: ActivatedRoute, protected router: Router, protected modalService: NgbModal) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ smartObject }) => {
            setTimeout(() => {
                this.ngbModalRef = this.modalService.open(SmartObjectDeleteDialogComponent as Component, {
                    size: 'lg',
                    backdrop: 'static'
                });
                this.ngbModalRef.componentInstance.smartObject = smartObject;
                this.ngbModalRef.result.then(
                    result => {
                        this.router.navigate([{ outlets: { popup: null } }], { replaceUrl: true, queryParamsHandling: 'merge' });
                        this.ngbModalRef = null;
                    },
                    reason => {
                        this.router.navigate([{ outlets: { popup: null } }], { replaceUrl: true, queryParamsHandling: 'merge' });
                        this.ngbModalRef = null;
                    }
                );
            }, 0);
        });
    }

    ngOnDestroy() {
        this.ngbModalRef = null;
    }
}
